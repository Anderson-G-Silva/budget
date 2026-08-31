package dio.budget.util;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RelativeDate {

    // Aceita dígitos ou texto por extenso para dias
    private static final Pattern HA_DIAS = Pattern.compile("\\bhá\\s+(\\d+|um|uma|dois|duas|três|tres|quatro|cinco|seis|sete|oito|nove|dez)\\s+dias?(?:\\s+atrás)?\\b");

    // Aceita dígitos ou texto por extenso para semanas
    private static final Pattern HA_SEMANAS = Pattern.compile("\\bhá\\s+(\\d+|uma|duas|três|tres|quatro|cinco|seis|sete|oito|nove|dez)\\s+semanas?(?:\\s+atrás)?\\b");

    private static final Map<String, Integer> EXTENSO_PARA_INT = Map.ofEntries(
            Map.entry("um", 1),
            Map.entry("uma", 1),
            Map.entry("dois", 2),
            Map.entry("duas", 2),
            Map.entry("três", 3),
            Map.entry("tres", 3),
            Map.entry("quatro", 4),
            Map.entry("cinco", 5),
            Map.entry("seis", 6),
            Map.entry("sete", 7),
            Map.entry("oito", 8),
            Map.entry("nove", 9),
            Map.entry("dez", 10)
    );

    @Tool(
            name = "converterDataRelativa",
            description = "Converte expressões de data relativa (ontem, anteontem, há X dias, há X semanas) em um LocalDate"
    )
    public LocalDate converter(
            @ToolParam(description = "Expressão de data relativa") String texto,
            @ToolParam(description = "Data de referência para o cálculo") LocalDate dataReferencia
    ) {
        if (texto == null || dataReferencia == null) {
            return null;
        }

        String t = texto.toLowerCase(new Locale("pt", "BR")).trim();

        if (t.contains("anteontem")) {
            return dataReferencia.minusDays(2);
        }

        if (t.contains("ontem")) {
            return dataReferencia.minusDays(1);
        }

        if (t.contains("hoje")) {
            return dataReferencia;
        }

        if (t.contains("amanhã") || t.contains("amanha")) {
            return dataReferencia.plusDays(1);
        }

        // Matcher para DIAS
        Matcher mDias = HA_DIAS.matcher(t);
        if (mDias.find()) {
            String capturado = mDias.group(1);
            int dias = converterParaInt(capturado);
            return dataReferencia.minusDays(dias);
        }

        // Matcher para SEMANAS (multiplica por 7)
        Matcher mSemanas = HA_SEMANAS.matcher(t);
        if (mSemanas.find()) {
            String capturado = mSemanas.group(1);
            int semanas = converterParaInt(capturado);
            return dataReferencia.minusWeeks(semanas);
        }

        return null;
    }

    private int converterParaInt(String valor) {
        if (EXTENSO_PARA_INT.containsKey(valor)) {
            return EXTENSO_PARA_INT.get(valor);
        }
        return Integer.parseInt(valor);
    }

    public static String formatar(LocalDate data) {
        if (data == null) return null;
        return data.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}