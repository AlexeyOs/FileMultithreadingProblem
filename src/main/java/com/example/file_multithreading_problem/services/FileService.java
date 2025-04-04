package com.example.file_multithreading_problem.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class FileService {
    static final Logger log = LoggerFactory.getLogger(FileService.class);
    public void parseValuesFromFileToDTO(List<MultipartFile> files) {
        CompletableFuture<Void> filesDtoFromFile = CompletableFuture.runAsync(() ->
        {
            for(MultipartFile file : files) {
                final Integer cellIndex = 0;
                final Integer sheetIndex = 0;
                final Integer limitValues = 100;
                try {
                    Set<String> emailsFromFile = parseCellValueByCellIndexAndSheetIndexWithLimitValues(cellIndex,
                            sheetIndex,
                            limitValues,
                            file.getBytes());
                    for (String email:emailsFromFile) {
                        log.info(email);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }, Executors.newSingleThreadExecutor()).exceptionally((e) -> {
            log.error("Ошибка парсинга значений из файла(ов)", e);
            return null;
        });
        filesDtoFromFile.thenRun(() -> log.info("Чтение данных из файла(ов) завершено"));
    }

    /***
     * Получение списка уникальных значений из файла из заданного листа и колонки по индексу с ограничением количества успешно считанных значений
     *
     * @param cellIndex  - индекс колонки файла.
     * @param sheetIndex - индекс листа файла.
     * @param limit      - ограничение по количеству значений итогового списка.
     *
     * @return Коллекция уникальных записей из файла.
     */
    public static Set<String> parseCellValueByCellIndexAndSheetIndexWithLimitValues(Integer cellIndex, Integer sheetIndex,
                                                                                    Integer limit, byte[] fileBytes) throws IOException {
        Set<String> values = new HashSet<>();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileBytes))) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            for (Row row : sheet) {
                Cell cell = row.getCell(cellIndex);
                if (!ObjectUtils.isEmpty(cell) && Objects.equals(cell.getCellType(), CellType.STRING)) {
                    String value = cell.getStringCellValue();
                    if (!ObjectUtils.isEmpty(value)) {
                        values.add(value.toLowerCase());
                    }
                    if (Objects.nonNull(limit) && values.size() >= limit) {
                        break;
                    }
                }
            }
        }
        return values;
    }
}
