package net.nemo.random_name_tag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    @JsonProperty("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("name")
        private Name name;

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public static class Name {
            @JsonProperty("title")
            private String title;

            @JsonProperty("first")
            private String first;

            @JsonProperty("last")
            private String last;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFirst() {
                return first;
            }

            public void setFirst(String first) {
                this.first = first;
            }

            public String getLast() {
                return last;
            }

            public void setLast(String last) {
                this.last = last;
            }
        }
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"gender\": \"female\",\n" +
                "            \"name\": {\n" +
                "                \"title\": \"Ms\",\n" +
                "                \"first\": \"Mónica\",\n" +
                "                \"last\": \"Martínez\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ApiResponse apiResponse = objectMapper.readValue(json, ApiResponse.class);
            for (Result result : apiResponse.getResults()) {
                Result.Name name = result.getName();
                System.out.println("Title: " + name.getTitle());
                System.out.println("First Name: " + name.getFirst());
                System.out.println("Last Name: " + name.getLast());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}