package org.jabref.model.entry.field;

import java.util.stream.Stream;

import org.jabref.model.entry.types.BiblatexApaEntryType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldFactoryTest {

    @Test
    void isValidFieldName() {
        assertTrue(FieldFactory.isValidFieldName("authors"));
        assertTrue(FieldFactory.isValidFieldName("comment-"));
        assertTrue(FieldFactory.isValidFieldName("__internal"));
        assertTrue(FieldFactory.isValidFieldName("field_name_without_spaces"));
    }

    @Test
    void isInvalidFieldName() {
        assertFalse(FieldFactory.isValidFieldName("authors{}"));
        assertFalse(FieldFactory.isValidFieldName("comment#"));
        assertFalse(FieldFactory.isValidFieldName("'internal"));
        assertFalse(FieldFactory.isValidFieldName("field name with spaces"));
        assertFalse(FieldFactory.isValidFieldName("notASCIIáéíóú"));
    }

    @Test
    void orFieldsTwoTerms() {
        assertEquals("Aaa/Bbb", FieldFactory.serializeOrFields(new UnknownField("aaa"), new UnknownField("bbb")));
    }

    @Test
    void orFieldsThreeTerms() {
        assertEquals("Aaa/Bbb/Ccc", FieldFactory.serializeOrFields(new UnknownField("aaa"), new UnknownField("bbb"), new UnknownField("ccc")));
    }

    private static Stream<Arguments> fieldsWithoutFieldProperties() {
        return Stream.of(
                // comment fields
                Arguments.of(new UserSpecificCommentField("user1"), "comment-user1"),
                Arguments.of(new UserSpecificCommentField("other-user-id"), "comment-other-user-id"),
                // unknown field
                Arguments.of(new UnknownField("cased", "cAsEd"), "cAsEd")
        );
    }

    @ParameterizedTest
    @MethodSource
    void fieldsWithoutFieldProperties(Field expected, String name) {
        assertEquals(expected, FieldFactory.parseField(name));
    }

    @Test
    void doesNotParseApaFieldWithoutEntryType() {
        assertNotEquals(BiblatexApaField.ARTICLE, FieldFactory.parseField("article"));
    }

    @Test
    void doesParseApaFieldWithEntryType() {
        assertEquals(BiblatexApaField.ARTICLE, FieldFactory.parseField(BiblatexApaEntryType.Constitution, "article"));
    }
}
