package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

import java.util.List;

public class Decryption {

    private long id;
    private String name;
    private String dispersion;
    private String url;
    private List<Accent> accents;

    public Decryption(long id, String name, String dispersion, String url, List<Accent> accents) {
        this.id = id;
        this.name = name;
        this.dispersion = dispersion;
        this.url = url;
        this.accents = accents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDispersion() {
        return dispersion;
    }

    public void setDispersion(String dispersion) {
        this.dispersion = dispersion;
    }

    public List<Accent> getAccents() {
        return accents;
    }

    public void setAccents(List<Accent> accents) {
        this.accents = accents;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static class Accent {
        private String name;
        private String positive;
        private String negative;
        private String description;
        private int multiple;

        private List<Dispersion> dispersions;

        private Accent() {

        }

        private Accent(String name, String positive, String negative, String description, int multiple, List<Dispersion> dispersions) {
            this.name = name;
            this.positive = positive;
            this.negative = negative;
            this.description = description;
            this.multiple = multiple;
            this.dispersions = dispersions;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPositive() {
            return positive;
        }

        public void setPositive(String positive) {
            this.positive = positive;
        }

        public String getNegative() {
            return negative;
        }

        public void setNegative(String negative) {
            this.negative = negative;
        }

        public int getMultiple() {
            return multiple;
        }

        public void setMultiple(int multiple) {
            this.multiple = multiple;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Dispersion> getDispersions() {
            return dispersions;
        }

        public void setDispersions(List<Dispersion> dispersions) {
            this.dispersions = dispersions;
        }
    }

    public static class Dispersion {
        private int stan;
        private String desc;

        public Dispersion(int stan, String desc) {
            this.stan = stan;
            this.desc = desc;
        }

        public int getStan() {
            return stan;
        }

        public void setStan(int stan) {
            this.stan = stan;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
