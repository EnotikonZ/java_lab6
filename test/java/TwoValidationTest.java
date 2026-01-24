import annotations.Two;
import examples.TwoExample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест №6 (Задание 2): проверка корректности обработки аннотации {@link Two}
 * при некорректно заданных свойствах (first = "", second < 0).
 */
class TwoValidationTest {

    /** Вспомогательный класс с некорректными параметрами аннотации @Two. */
    @Two(first = "", second = -1)
    static class InvalidTwoAnnotatedClass { }

    /** Вспомогательный класс с корректными параметрами аннотации @Two. */
    @Two(first = "ok", second = 1)
    static class ValidTwoAnnotatedClass { }

    /**
     * Метод-валидатор из условия: через Reflection считывает значения аннотации @Two
     * и выбрасывает IllegalArgumentException при нарушении условий.
     */
    private static void validateTwoAnnotation(Class<?> clazz) {
        Two ann = clazz.getAnnotation(Two.class);
        if (ann == null) {
            throw new IllegalArgumentException("Аннотация @Two не найдена на классе: " + clazz.getName());
        }

        String first = ann.first();
        int second = ann.second();

        if (first == null || first.isEmpty()) {
            throw new IllegalArgumentException("Некорректное значение first: пустая строка");
        }
        if (second < 0) {
            throw new IllegalArgumentException("Некорректное значение second: " + second);
        }
    }

    @Test
    @DisplayName("Выбрасывается IllegalArgumentException при first="" и second<0")
    void invalidTwoPropertiesMustThrow() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validateTwoAnnotation(InvalidTwoAnnotatedClass.class)
        );

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("first") || ex.getMessage().contains("second"));
    }

    @Test
    @DisplayName("Корректные параметры @Two проходят валидацию без исключений")
    void validTwoPropertiesMustNotThrow() {
        assertDoesNotThrow(() -> validateTwoAnnotation(ValidTwoAnnotatedClass.class));
        // Также проверим существующий пример из проекта
        assertDoesNotThrow(() -> validateTwoAnnotation(TwoExample.class));
    }
}
