/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaloz.datafeed.yahoo.infrastructure.integration.dataformat;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.io.Serializable;

@CsvRecord(separator = ",", crlf = "UNIX")
public class YahooCSV implements Serializable {

//    JNJ, Johnson & Johnson, 7/25/2013, 92.57, 2.70, 20.50

    @DataField(pos = 1, trim = true)
    private String shortName;

    @DataField(pos = 2, trim = true)
    private String longName;

    @DataField(pos = 3, trim = true)
    private String date;

    @DataField(pos = 4, trim = true)
    private String lastPrice;

    @DataField(pos = 5, trim = true)
    private String dividendYield;

    @DataField(pos = 6, trim = true)
    private String peRatio;

    public void setPeRatio(String peRatio) {
        this.peRatio = peRatio;
    }

    public void setDividendYield(String dividendYield) {
        this.dividendYield = dividendYield;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getDate() {
        return date;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getDividendYield() {
        return dividendYield;
    }

    public String getPeRatio() {
        return peRatio;
    }

    @Override
    public String toString() {
        return "YahooCSV{" +
                "shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", date=" + date +
                ", lastPrice=" + lastPrice +
                ", dividendYield=" + dividendYield +
                ", peRatio=" + peRatio +
                '}';
    }
}
