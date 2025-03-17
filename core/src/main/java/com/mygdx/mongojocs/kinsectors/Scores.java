package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Scores {
    /**
     * An array of player names.
     */
    public  String fichero = "scores";
    public String[] names = {"MIC", "ROJ", "OCS"};
    /**
     * An array of player high scores.
     */
    public int[] values = {500, 300, 200};
    //public int[] values = {30, 20, 10};

    /**
     * Utility method to convert a integer score to a character
     * array.
     
    public static void toCharArray(int score, char[] charArray) {
        // Convert the score to an array of chars
        // of the form: 0000
        charArray[0] = (char)('0' + (score / 1000));
        score = score % 1000;
        charArray[1] = (char)('0' + (score / 100));
        score = score % 100;
        charArray[2] = (char)('0' + (score / 10));
        score = score % 10;
        charArray[3] = (char)('0' + score);
    }*/

    public Scores(String f) {

         fichero = f;
        // Initialize / read scores from persistent storage.
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore(fichero, true);

            // If the record store exists and contains records,
            // read the high scores.
            if (recordStore.getNumRecords() > 0) {
                for (int i = 0; i < names.length; i++) {
                    byte[] record = recordStore.getRecord(i + 1);
                    DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record, 0, record.length));
                    values[i] = istream.readInt();
                    names[i] = istream.readUTF();
                }
            } else {
                // Otherwise, create the records and initialize them
                // with the default values. They will have record IDs
                // 1, 2, 3
                for (int i = 0; i < names.length; i++) {
                    ByteArrayOutputStream bstream = new ByteArrayOutputStream(12);
                    DataOutputStream ostream = new DataOutputStream(bstream);
                    ostream.writeInt(values[i]);
                    ostream.writeUTF(names[i]);
                    ostream.flush();
                    ostream.close();
                    byte[] record = bstream.toByteArray();
                    recordStore.addRecord(record, 0, record.length);
                }
            }
            if (recordStore != null) recordStore.closeRecordStore();
        } catch(Exception e) {}
        
       /* } finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                } catch(Exception e) {
                }
            }
        }*/
    }

    /**
     * Returns true if the score is among the high scores.
     */
    public boolean isHighScore(int score) {
        for (int i = 0; i < names.length; i++) {
            if (score >= values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the high score database with the supplied name and score.
     */
    public void addHighScore(int score, String name) {
        for (int i = 0; i < names.length; i++) {
            if (score >= values[i]) {
                // Shift the score table.
                for (int j = names.length - 1; j > i; j--) {
                    values[j] = values[j - 1];
                    names[j] = names[j - 1];
                }

                // Insert the new score.
                values[i] = score;
                names[i] = name;

                // Overwrite the scores in persistent storage.
                RecordStore recordStore = null;
                try {
                    recordStore = RecordStore.openRecordStore(fichero, true);
                    for (int j = 0; j < names.length; j++) {
                        ByteArrayOutputStream bstream = new ByteArrayOutputStream(12);
                        DataOutputStream ostream = new DataOutputStream(bstream);
                        ostream.writeInt(values[j]);
                        ostream.writeUTF(names[j]);
                        ostream.flush();
                        ostream.close();
                        byte[] record = bstream.toByteArray();
                        recordStore.setRecord(j + 1, record, 0, record.length);
                    }
                    if (recordStore != null) recordStore.closeRecordStore();
                } catch(Exception e) {}
                
       /*         } finally {
                    if (recordStore != null) {
                        try {
                            recordStore.closeRecordStore();
                        } catch(Exception e) {
                        }
                    }
                }*/
                break;
            }
        }
    }
}
