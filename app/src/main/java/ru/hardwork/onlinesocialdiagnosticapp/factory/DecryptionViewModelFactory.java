package ru.hardwork.onlinesocialdiagnosticapp.factory;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Decryption;

public class DecryptionViewModelFactory implements IFactory<DescriptionViewModel> {
    private static final String SPLITTER = ",";

    private ArrayList<Integer> processing;
    private Decryption decryption;

    public DecryptionViewModelFactory() {

    }

    public DecryptionViewModelFactory(ArrayList<Integer> processing, Decryption decryption) {
        this.processing = processing;
        this.decryption = decryption;
    }


    public void setProcessing(ArrayList<Integer> processing) {
        this.processing = processing;
    }

    public void setDecryption(Decryption decryption) {
        this.decryption = decryption;
    }

    @Override
    public List<DescriptionViewModel> build() {
        List<DescriptionViewModel> result = new ArrayList<>();
        // Получение шкал
        for (Decryption.Accent accent : decryption.getAccents()) {
            int semi = 0;
            int max = 0;
            String[] positive = StringUtils.split(accent.getPositive(), SPLITTER);
            if (positive != null) {
                for (String s : positive) {
                    int i = Integer.parseInt(StringUtils.trim(s)) - 1;
                    if (processing.size() <= i) {
                        Log.e("Done:", "Не найден вариант для метрики");
                        continue;
                    }

                    if (processing.get(i) == 1) {
                        semi++;
                    }
                    max++;
                }
            }
            // Вычисляем из отрицательных ответов
            String[] negative = StringUtils.split(accent.getNegative(), SPLITTER);
            if (negative != null) {
                for (String s : negative) {
                    int i = Integer.parseInt(StringUtils.trim(s)) - 1;
                    if (processing.size() <= i) {
                        Log.e("Done:", "Не найден вариант для метрики");
                        continue;
                    }

                    if (processing.get(i) == 0) {
                        semi++;
                    }
                    max++;
                }
            }
            // Получаем нормализованное значение
            semi *= accent.getMultiple();
            max *= accent.getMultiple();
            DescriptionViewModel desc = new DescriptionViewModel(accent.getName(), max, semi);
            result.add(desc);
        }

        return result;
    }
}