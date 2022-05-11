package org.minsirv;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JarValidatorTest {

    private final JarValidator validator = new JarValidatorImpl();

    @Test
    public void acceptableCode() {
        Assert.assertTrue(validator.validate("34409157850"));
    }

    @Test
    public void testMinusSign() {
        Assert.assertFalse(validator.validate("-3440915785"));
    }

    @Test
    public void testNonDigits() {
        Assert.assertFalse(validator.validate("A4409157850"));
    }

    @Test
    public void testNotElevenCharacters() {
        Assert.assertFalse(validator.validate("334409157850"));
        Assert.assertFalse(validator.validate("3440915785"));
    }

    @Test
    public void testGenderCode() {
//        invalid gender codes
        Assert.assertFalse(validator.validate("04409157850"));
        Assert.assertFalse(validator.validate("14409157850"));
        Assert.assertFalse(validator.validate("24409157850"));
        Assert.assertFalse(validator.validate("74409157850"));
        Assert.assertFalse(validator.validate("84409157850"));
        Assert.assertFalse(validator.validate("94409157850"));

//        valid gender codes
        Assert.assertTrue(validator.validate("34409157850"));
        Assert.assertTrue(validator.validate("44409157850"));
        Assert.assertTrue(validator.validate("54409157850"));
        Assert.assertTrue(validator.validate("64409157850"));
    }

    @Test
    public void testBirthDateCode() {
//        month 00
        Assert.assertFalse(validator.validate("34400157850"));
//        month 13
        Assert.assertFalse(validator.validate("34413157850"));
//        day 00
        Assert.assertFalse(validator.validate("34409007850"));
//        day 32
        Assert.assertFalse(validator.validate("34409327850"));

        Assert.assertTrue(validator.validate("34401017859"));
    }

    @Test
    public void testControlCode()  {
        Assert.assertTrue(validator.validate("34401017859"));
        Assert.assertTrue(validator.validate("35112013388"));

        Assert.assertFalse(validator.validate("34401017858"));
        Assert.assertFalse(validator.validate("45611139673"));
    }

    @Test
    public void isMaleTest() {
        Assert.assertTrue(validator.isMale("34401017859"));
        Assert.assertFalse(validator.isMale("44401017859"));

        Assert.assertTrue(validator.isMale("54401017859"));
        Assert.assertFalse(validator.isMale("64401017859"));
    }

    @Test
    public void isFemaleTest() {
        Assert.assertFalse(validator.isFemale("34401017859"));
        Assert.assertTrue(validator.isFemale("44401017859"));

        Assert.assertFalse(validator.isFemale("54401017859"));
        Assert.assertTrue(validator.isFemale("64401017859"));
    }

    @Test
    public void toDateTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Assert.assertEquals(validator.toDate("34401017859"), format.parse("1944-01-01"));
        Assert.assertEquals(validator.toDate("44411017859"), format.parse("1944-11-01"));
        Assert.assertEquals(validator.toDate("54405187859"), format.parse("2044-05-18"));
        Assert.assertEquals(validator.toDate("61105057859"), format.parse("2011-05-05"));
    }
}
