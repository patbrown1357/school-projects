import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CarTracker {

  private static TreeMap<String, IndHeap> pTree = new TreeMap<String, IndHeap>();
  private static TreeMap<String, IndHeap> mTree = new TreeMap<String, IndHeap>();
  private static TreeMap<String, Car> vins = new TreeMap<String, Car>();

  private static IndHeap pHeap = new IndHeap("p");
  private static IndHeap mHeap = new IndHeap("m");
  private static String[] attrs = { "VIN", "Make", "Model", "Price","Mileage","Color"};
  private static Scanner userIn = new Scanner( System.in );

  public static class Car {
    //VIN:Make:Model:Price:Mileage:Color
    private String[] carAttr = new String[6];
    //[price,mileage]
    int mHeapInd;
    int pHeapInd;
    int mmmHeapInd;
    int mmpHeapInd;

    public Car() {}
    public Car(String[] attr) {
      carAttr = attr;
    }

    public void setMHeapInd(int x) { mHeapInd = x; }
    public void setPHeapInd(int x) { pHeapInd = x; }
    public void setMMMHeapInd(int x) { mmmHeapInd = x; }
    public void setMMPHeapInd(int x) { mmpHeapInd = x; }

    public String getVin() { return carAttr[0]; }
    public String getMake() { return carAttr[1]; }
    public String getModel() { return carAttr[2]; }
    public String getPrice() { return carAttr[3]; }
    public String getMileage() { return carAttr[4]; }
    public String getColor() { return carAttr[5]; }
    public String getMM() {
      return getMake().toLowerCase() + getModel().toLowerCase();
    }

    public void setVin( String newVal ) { this.carAttr[0] = newVal; }
    public void setMake( String newVal ) {  this.carAttr[1] = newVal;}
    public void setModel( String newVal ) {  this.carAttr[2] = newVal; }
    public void setPrice( String newVal ) {  this.carAttr[3] = newVal; }
    public void setMileage( String newVal ) {  this.carAttr[4] = newVal; }
    public void setColor( String newVal ) {  this.carAttr[5] = newVal; }

    public int getMInd() { return mHeapInd; }
    public int getPInd() { return pHeapInd; }
    public int getMMMInd() { return mmmHeapInd; }
    public int getMMPInd() { return mmpHeapInd; }

    public String toString() {
      String print = "";
      for( int i = 0; i < 6; i++) {
        print += attrs[i] +": " + carAttr[i] + "\n";
      }
      return print;
    }

  }



  public static class IndHeap {
    public int end = 0;

    //2^8-1 or 8 layers in our heap
    Car[] heap = new Car[255];
    String heapType;

    //all heaps need a type
    public IndHeap(String type)  { heapType = type; }

    public void add(Car newCar) {
      this.end++;
      heap[end] = newCar;
      swim(end);
      if( end == heap.length ) { resize(); }
    }

    public void remove(int ind) {
      exch(ind, end--);
      heap[end+1] = null;
      sink(ind);

    }

    //will neeed to try and go up and down to account
    //for how the value was updated
    public void update(int ind) {
      swim(ind);
      sink(ind);
    }

    private void sink( int ind ) {
      while( 2*ind <= end ) {
        int i = 2*ind;
        if(i < end && less( i+1, i ))  i++;
        if(!less(i, ind)) break;
        exch(ind, i);
        ind = i;
      }
    }

    private void swim(int ind) {
      while( ind > 1 && less(ind, ind/2)) {
        exch(ind/2, ind);
        ind = ind/2;
      }

    }

    //if we end up with 0 just return as we leave that emtpy
    private boolean less( int i, int j ) {
      if( i == 0 || j == 0) return false;

      int secVal = 0;
      int firstVal = 0;

      if( heapType.equals("m") || heapType.equals("mmm")) {
        secVal = Integer.parseInt( heap[j].getMileage());
        firstVal = Integer.parseInt( heap[i].getMileage());
      } else if( heapType.equals("p") || heapType.equals("mmp")) {
        secVal = Integer.parseInt( heap[j].getPrice());
        firstVal = Integer.parseInt( heap[i].getPrice());
      }

      return ( firstVal - secVal ) < 0;
    }

    private void exch( int i, int j ) {

      Car temp = this.heap[i];
      this.heap[i] = this.heap[j];
      this.heap[j] = temp;

      if( heapType.equals("m")) {
        this.heap[j].mHeapInd = j;
        this.heap[i].mHeapInd = i;
      } else if( heapType.equals("p")) {
        this.heap[j].pHeapInd = j;
        this.heap[i].pHeapInd = i;
      } else if( heapType.equals("mmm")) {
        this.heap[j].mmmHeapInd = j;
        this.heap[i].mmmHeapInd = i;
      } else if( heapType.equals("mmp")) {
        this.heap[j].mmpHeapInd = j;
        this.heap[i].mmpHeapInd = i;
      }
    }
    private void resize() {

      Car[] newCars = new Car[2*this.heap.length];
      for( int i = 0; i < this.heap.length; i++ ) {
        newCars[i] = this.heap[i];
      }
      this.heap = newCars;
    }

    public Car retrieveMin() {
      return heap[1];
    }
  }

//---------------------------------------------------------------------------------


  public static Car makeCar() {

    Car newCar = new Car();
    int i = 0;
    System.out.print( "Enter a 17 character VIN: ");
    newCar.setVin(userIn.next());
    System.out.print( "Enter the make of the car: ");
    newCar.setMake(userIn.next());
    System.out.print( "Enter the model of the car: ");
    newCar.setModel(userIn.next());
    System.out.print( "Enter the price of the car: ");
    newCar.setPrice(checkInt());
    System.out.print( "Enter the mileage on the car: ");
    newCar.setMileage(checkInt());
    System.out.print("Enter the color of the car: ");
    newCar.setColor(userIn.next());

    return newCar;
  }

  private static void addCar( Car newCar ) {

    String mmString = newCar.getMM();

    if( !pTree.containsKey(mmString)) {
      pTree.put(mmString, new IndHeap("mmp"));
      mTree.put(mmString, new IndHeap("mmm"));
      mTree.get(mmString).add(newCar);
      pTree.get(mmString).add(newCar);

    } else {
      mTree.get(mmString).add(newCar);
      pTree.get(mmString).add(newCar);
    }

    vins.put( newCar.getVin(), newCar );

    pHeap.add(newCar);
    mHeap.add(newCar);

  }

  private static void removeCar() {
    String vin = "";
    //make sure they're removing a valid car
    while( !vins.containsKey(vin) ) {
      System.out.print("Please enter a valid VIN: ");
      vin = userIn.next();
    }

    Car delCar = vins.get(vin);

    pHeap.remove(delCar.getPInd());
    mHeap.remove(delCar.getMInd());
    pTree.get(delCar.getMM()).remove(delCar.getMMPInd());
    mTree.get(delCar.getMM()).remove(delCar.getMMMInd());
    vins.remove(vin);
  }

  public static void update() {
    String vin = null;
    //make sure they are updating a valid car
    while( vin == null || !vins.containsKey(vin)) {
      System.out.print("Please enter a valid vin: ");
      vin = userIn.next();
    }

    Car updateCar = vins.get(vin);
    boolean update = true;
    //in case they want to update multiple values
    //i.e. new color on a car also decreases the value
    while(update) {
      System.out.print( "\nWould you like to update: \n" +
                        " 1) The price of the car \n"   +
                        " 2) The mileage on the car \n" +
                        " 3) The color of the car \n"   +
                        " 4) quit \n" +
                        " Please enter a number: ");

     int choice = Integer.parseInt(checkInt());

     switch( choice ) {
       case 1:
        System.out.print( "Current Price: " + updateCar.getPrice() + "\n New Price: ");
        updateCar.setPrice(userIn.next());
        pHeap.update( updateCar.getPInd());
        pTree.get(updateCar.getMM()).update(updateCar.getMMPInd());
        //price of the car
        break;
      case 2:
        System.out.print( "Current Mileage: " + updateCar.getMileage() + "\n New Mileage: ");
        updateCar.setMileage(userIn.next());
        mHeap.update(updateCar.getMInd());
        mTree.get(updateCar.getMM()).update(updateCar.getMMMInd());
        //mileage of the car
        break;

      case 3:
        System.out.print( "Current Color: " + updateCar.getColor() + "\n New Color: ");
        updateCar.setColor(userIn.next());
        //color of the car
        break;
      case 4:
          update = false;
        break;
      default:
        System.out.println( "Invalid choice please select another option: ");
        break;

      }
    }
  }
  //retrieval operations
  //retrieve min price or mileage
  public static void retrieve( IndHeap heap ) {
    System.out.print( heap.heap[1].toString());
  }

  //retrieve min price or mileage by make and model
  public static void retrieveMM( TreeMap tree ) {
    boolean invalidMM = true;
    String makeModel = "";
    while(invalidMM) {
      System.out.print("Please enter the make of the car: ");
      String make = userIn.next();
      System.out.print("Please enter the model of the car: ");
      String model = userIn.next();
      makeModel = make.toLowerCase() + model.toLowerCase();
      if(tree.containsKey(makeModel)) {
        invalidMM = false;
      } else {
        System.out.println("Make and Model not Found\n" +
                           "Please Try again ");
      }
    }

    IndHeap heap = (IndHeap) tree.get(makeModel);
    System.out.println(heap.retrieveMin().toString());
  }

  //input scrubber, make sure ints are valid
  public static String checkInt() {
    boolean invalid = true;
    String input = "";
    while(invalid) {
      input = userIn.next();
      try {
        Integer.parseInt(input);
        invalid = false;
      } catch( Exception NumberFormatException ) {
        System.out.print("Invalid Input\nPlease enter a number:");
      } finally {

      }
    }
    return input;
  }

  //check if there are cars
  public static boolean noCars() {
    return vins.isEmpty();
  }


  public static void main( String[] args ) throws FileNotFoundException {
    boolean running = true;

    //read in from a file
    if( args.length != 0 ) {
      Scanner fileIn = new Scanner( new File( args[0] ));
      if( fileIn.hasNextLine() ) fileIn.nextLine();
      while( fileIn.hasNextLine() ) {
        String[] carArr = new String[6];
        String carDesc = fileIn.nextLine();
        carArr = carDesc.split(":");
        Car newCar = new Car( carArr );
        addCar( newCar );
      }
    }

    while( running ) {

      System.out.println("\nOptions: ");
      System.out.print(" 1) add a Car\n" +
                       " 2) update a car\n" +
                       " 3) Remove a car of your choice \n" +
                       " 4) retrieve the lowest price car\n" +
                       " 5) retrieve the lowest mileage car\n" +
                       " 6) retrieve the lowest price car by make and Model\n" +
                       " 7) retrieve the lowest Mileage car by make and model\n" +
                       " 8) exit \n" +
                       " Please enter a number: " );

      int choice = Integer.parseInt(checkInt());
      System.out.println();

      switch( choice ) {
        case 1:
          //make and add a car
          Car newCar = makeCar();
          addCar(newCar);
          break;
        case 2:
          if(noCars()) {System.out.println("There are no cars to update"); break;}
          update();
          //update a car
          break;
        case 3:
        if(noCars()) {System.out.println("There are no cars to remove"); break;}
          removeCar();
          //remove a car
          break;
        case 4:
          if(noCars()) {System.out.println("There are no cars to retrieve"); break;}
          //retrieve the lowest price
          retrieve(pHeap);
          break;
        case 5:
          if(noCars()) {System.out.println("There are no cars to retrieve"); break;}
          //retrieve the lowest mileage
          retrieve(mHeap);
          break;
        case 6:
          if(noCars()) {System.out.println("There are no cars to retrieve"); break;}
          //retrieve the lowest p by m and m
          retrieveMM(pTree);
          break;
        case 7:
          if(noCars()) {System.out.println("There are no cars to retrieve"); break;}
          retrieveMM(mTree);
          //retrieve lowest mileage by m and m
          break;
        case 8:
          running = false;
          break;
        default:
          //invalid choice
          System.out.print("\nInvalid choice\nPlease choose again: ");
          break;

      }
    }

  }

}
