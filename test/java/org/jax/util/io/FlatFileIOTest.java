/*
 * Copyright (c) 2010 The Jackson Laboratory
 * 
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jax.util.io;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tester class for {@link FlatFileReader} and {@link FlatFileWriter}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FlatFileIOTest
{
    /**
     * The basic IO test
     * @throws IOException
     *          if we get an exception
     * @throws IllegalFormatException
     *          if the formatting is bad
     */
    @Test
    public void basicIOTest() throws IOException, IllegalFormatException
    {
        System.out.println("----");
        System.out.println("non-standard CSV with unix style line delimiters");
        this.basicIOTest(
                new String[] {"\n"},
                0,
                ',',
                '\'',
                '#');
        
        System.out.println("----");
        System.out.println("Standards based CSV with windows line delimiter");
        this.basicIOTest(
                new String[] {"\r\n"},
                0,
                ',',
                '\"',
                FlatFileFormat.NA_CHAR);
        
        System.out.println("----");
        System.out.println("tab delimited");
        this.basicIOTest(
                new String[] {"\n"},
                0,
                '\t',
                '\"',
                FlatFileFormat.NA_CHAR);
        
        System.out.println("----");
        System.out.println("something wild and crazy!!");
        this.basicIOTest(
                new String[] {"@i'm a crazy line delimiter dude@"},
                0,
                ',',
                '\"',
                FlatFileFormat.NA_CHAR);
        
        System.out.println("----");
        System.out.println("windows and unix detectable using windows separator");
        this.basicIOTest(
                new String[] {"\r\n", "\n"},
                0,
                ',',
                '\"',
                FlatFileFormat.NA_CHAR);
        
        System.out.println("----");
        System.out.println("windows and unix detectable using unix separator");
        this.basicIOTest(
                new String[] {"\r\n", "\n"},
                1,
                ',',
                '\"',
                '#');
    }
    
    /**
     * The basic IO test where we expect an {@link IOException}
     * @throws IOException
     *          if we get an exception
     * @throws IllegalFormatException
     *          if the formatting is bad
     */
    @Test(expected=IOException.class)
    public void basicIOFailureTest() throws IOException, IllegalFormatException
    {
        System.out.println("----");
        System.out.println("this should fail since we're not permitting quotes");
        this.basicIOTest(
                CommonFlatFileFormat.UNQUOTED_TAB_DELIMITED_UNIX.getRowDelimiterChoices(),
                0,
                CommonFlatFileFormat.UNQUOTED_TAB_DELIMITED_UNIX.getFieldDelimiter(),
                CommonFlatFileFormat.UNQUOTED_TAB_DELIMITED_UNIX.getQuoteChar(),
                CommonFlatFileFormat.UNQUOTED_TAB_DELIMITED_UNIX.getCommentChar());
    }
    
    private void basicIOTest(
            CharSequence[] rowDelimiterChoices,
            int delimiterChoiceToUse,
            char fieldDelimiter,
            int quoteChar,
            int commentChar) throws IOException, IllegalFormatException
    {
        String quoteString = (quoteChar == FlatFileFormat.NA_CHAR ? "" : Character.toString((char)quoteChar));
        CharSequence rowDelimiter = rowDelimiterChoices[delimiterChoiceToUse];
        String[][] dataToEncodeAndDecode = new String[][] {
                {
                    "inner row" + rowDelimiter + "delimiter 1" + quoteString + "with quote",
                    "inner field" + fieldDelimiter + "delimiter" + quoteString + "with quote",
                    "the plain field",
                    "inner row" + rowDelimiter + "delimiter 2"
                },
                {
                    "the",
                    "plain",
                    "row",
                    "is this one"
                },
                {
                    " it's the plain field on row 3 ",
                    "ends in quote" + quoteString,
                    quoteString + "starts with quotes",
                    "\tthe end\t"
                },
                {
                    "" + rowDelimiter,
                    "" + fieldDelimiter,
                    "" + quoteString,
                    "this row was pretty wacky huh?"
                }
        };
        
        this.encodeDecodeTest(
                dataToEncodeAndDecode,
                rowDelimiterChoices,
                delimiterChoiceToUse,
                fieldDelimiter,
                quoteChar,
                commentChar);
    }

    private void encodeDecodeTest(
            String[][] dataToEncodeAndDecode,
            CharSequence[] rowDelimiterChoices,
            int delimiterChoiceToUse,
            char fieldDelimiter,
            int quoteChar,
            int commentChar) throws IOException, IllegalFormatException
    {
        CharSequence rowDelimiter = rowDelimiterChoices[delimiterChoiceToUse];
        CharArrayWriter writer = new CharArrayWriter();
        
        FlatFileWriter flatFileWriter = new FlatFileWriter(
                writer,
                rowDelimiter,
                fieldDelimiter,
                quoteChar);
        
        for(String[] row: dataToEncodeAndDecode)
        {
            flatFileWriter.writeRow(row);
        }
        flatFileWriter.flush();
        flatFileWriter.close();
        
        System.out.println("Encoded Flat File");
        System.out.println(new String(writer.toCharArray()));
        System.out.println();
        
        CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        FlatFileReader flatFileReader = new FlatFileReader(
                reader,
                rowDelimiterChoices,
                fieldDelimiter,
                quoteChar,
                commentChar);
        String[][] decodedData = new String[dataToEncodeAndDecode.length][];
        
        System.out.println("Decoded Flat File:");
        String[] readRow;
        for(int i = 0; (readRow = flatFileReader.readRow()) != null; i++)
        {
            decodedData[i] = readRow;
            for(int j = 0; j < readRow.length; j++)
            {
                if(j >= 1)
                {
                    System.out.print(',');
                }
                System.out.print("[" + readRow[j] + "]");
            }
            System.out.println();
        }
        System.out.println();
        
        for(int i = 0; i < dataToEncodeAndDecode.length; i++)
        {
            Assert.assertArrayEquals(
                    dataToEncodeAndDecode[i],
                    decodedData[i]);
        }
    }
}
