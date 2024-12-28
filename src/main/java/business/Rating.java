package business;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Rating {
        private int userID;
        private int songID;
        private int rating;


    }
