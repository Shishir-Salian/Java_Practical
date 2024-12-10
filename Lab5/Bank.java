interface Bankinterface 
{
    public void setbalance(float balance) ;
    public void setinterestrate(float interestrate) ;
    public float getbalance() ;
    public float getinterestrate() ;
}

public class Bank implements Bankinterface 
{
    float interestrate = 0;
    float balance =0;

    public float getbalance() 
    {
        return balance;
    }
    public float getinterestrate() 
    {
        return interestrate;
    }

    public void setbalance(float balance) 
    {
        this.balance = balance;
    }
    public void setinterestrate(float interestrate) 
    {
        this.interestrate = interestrate;
    }


    public static void main(String[] args) 
    {
        Bank bankA = new Bank();
        bankA.setbalance(10000);
        bankA.setinterestrate(7f);
		System.out.println("Bank A");
        System.out.println("Balance: " + bankA.getbalance());
        System.out.println("Interest Rate: " + bankA.getinterestrate());

        Bank bankB = new Bank();
		System.out.println("Bank B");
        bankB.setbalance(150000);
        bankB.setinterestrate(7.4f);
        System.out.println("Balance: " + bankB.getbalance());
        System.out.println("Interest Rate: " + bankB.getinterestrate());

        Bank bankC = new Bank();
		System.out.println("Bank C");
        bankC.setbalance(200000);
        bankC.setinterestrate(7.9f);
        System.out.println("Balance: " + bankC.getbalance());
        System.out.println("Interest Rate: " + bankC.getinterestrate());
    }
}