package models;
public class Customer  {
   private String PlateNum ;
   private int id ;
   public Customer (int id ,String PlateNum){
      this.id=id ;
      this.PlateNum=PlateNum ;
   }
   public String GetPlatenum(){
      return PlateNum ;
   }
   public int GetId (){
      return id ;
   }
   public void SetPlatenum(String PlateNum){
      this.PlateNum=PlateNum ;
   }
   public void SetId(int id ){
      this.id=id ;
   }
   
    public void display() {
        System.out.println("Customer ID: " + id + " | Plate: " + PlateNum);
    }


}
//Customer is the driver/car owner  NOT a system user we only need (plate no, id)