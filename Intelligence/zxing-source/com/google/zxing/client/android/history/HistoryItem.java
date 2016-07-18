/*
 * Copyright 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.history;

import java.sql.Timestamp;

import com.google.zxing.Result;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName="historico")
public  class HistoryItem {

	@DatabaseField(id=true)
	private long id;
	@DatabaseField(columnName="qrcode")
	private Result result;
	@DatabaseField
	private String display;
	@DatabaseField
	private String format;
	@DatabaseField
	private Timestamp timestamp;

	public HistoryItem() {
//		deixe um contrutor vazio
	}

	public HistoryItem(long id, Result result, String display, String format,
			Timestamp timestamp) {
		this.id = id;
		this.result = result;
		this.display = display;
		this.format = format;
		this.timestamp = timestamp;
	}

	public HistoryItem(Result result, String display) {
		this.result = result;
		this.display = display;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Result getResult() {
		return result;
	}

	public String getDisplay() {
		StringBuilder displayResult = new StringBuilder();
		if (display != null || display.isEmpty()) {
			displayResult.append(display);
		}
		return display;
	}

	public String getDisplayAndDetails() {
		StringBuilder displayResult = new StringBuilder();
		if (display == null || display.isEmpty()) {
			displayResult.append(result.getText());
		} else {
			displayResult.append(display);
		}
		return displayResult.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryItem other = (HistoryItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HistoryItem [id=" + id + ", result=" + result + ", display="
				+ display + ", format=" + format + ", timestamp=" + timestamp
				+ "]";
	}
}
