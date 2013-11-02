/*
 * Copyright 2013 Krisztian Lachata
 *
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

package org.kaloz.messaging.app.client;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.kaloz.messaging.infrastructure.integration.JsonMessageRegistry;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties({"createdTimestamp","errorCode","errorMessage","messageType"})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public abstract class JsonMessage implements Serializable {

	private static final long serialVersionUID = -1;
    public static final String CREATED_TIMESTAMP = "createdTimestamp";
    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_MESSAGE = "errorMessage";

    @Resource
    public void registerMessage(JsonMessageRegistry jsonMessageRegistry){
        jsonMessageRegistry.registerMessage(getMessageType(), this.getClass());
    }

    @JsonProperty
    private Map<String, Object> headers;

    protected JsonMessage() {
    }

    public Date getCreatedTimestamp() {
        return (Date)headers.get(CREATED_TIMESTAMP);
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        addHeaderElement(CREATED_TIMESTAMP, createdTimestamp);
    }

    public Integer getErrorCode() {
        return (Integer)headers.get(ERROR_CODE);
    }

    public void setErrorCode(Integer errorCode) {
        addHeaderElement(ERROR_CODE, errorCode);
    }

    public String getErrorMessage() {
        return (String)headers.get(ERROR_MESSAGE);
    }

    public void setErrorMessage(String errorMessage) {
        addHeaderElement(ERROR_MESSAGE, errorMessage);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public boolean hasHeader() {
        return headers != null && !headers.isEmpty();
    }

    public void setHeaders(Map<String, Object> header) {
        this.headers = header;
    }

    public void addHeaders(Map<String, Object> headers) {
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
    		addHeaderElement(entry.getKey(), entry.getValue());
        }
    }

    public void addHeaderElement(String key, Object value) {
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        headers.put(key, value);
    }

    public void clearHeader() {
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        headers.clear();
    }

    @SuppressWarnings("unchecked")
    public <T> T getHeaderElement(String key) {
        if (headers == null) {
            return null;
        }
        return (T) headers.get(key);
    }

    public abstract String getMessageType();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof JsonMessage))
            return false;
        JsonMessage castOther = (JsonMessage) other;
        return new EqualsBuilder().append(headers, castOther.headers).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(headers).toHashCode();
    }

}
