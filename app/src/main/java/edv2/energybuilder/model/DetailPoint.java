package edv2.energybuilder.model;

public class DetailPoint {
        String name = "";
        int total = 0;
        String type = "";

        public DetailPoint() {
        }

        public DetailPoint(String name, int total, String type) {
            this.name = name;
            this.total = total;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
}
