import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Order {
   private String firstName;
   private String lastName;
   private String address;
   private Integer metroStation;
   private String phone;
   private Integer rentTime;
   private String deliveryDate;
   private String comment;
   private String[] color;
}
