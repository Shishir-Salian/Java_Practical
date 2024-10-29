public class AlphabetWar
{
    AlphabetWar()
    {
        
    }
    AlphabetWar(int Health)
    {
        Health1 = Health;
        Health2 = Health;
    }
    private int turn = 0;
    private int damage = 0;
    private final char[][] strength1 = {{'w', '4'}, {'p', '3'}, {'b', '2'}, {'s', '1'}};
    private final char[][] strength2 = {{'m', '4'}, {'q', '3'}, {'d', '2'}, {'z', '1'}};
    int Health1;
    int Health2;
    private void play(char warrior)
    {
        turn++;
        if(turn%2==1)
        {
            for(int i = 0;i<strength1.length;i++)
            {
                if(strength1[i][0]==warrior)
                {
                    damage = Character.getNumericValue(strength1[i][1]);
                }
            }
        }
        else if(turn%2==0)
        {
            for(int i = 0;i<strength2.length;i++)
            {
                if(strength2[i][0]==warrior)
                {
                    damage = damage - Character.getNumericValue(strength2[i][1]);
                }
            }
            if(damage<0)
            {
                System.out.println("Player 2 Has damaged Player 1 by "+(damage*-1));
                Health1 = Health1 - (damage*-1);
                damage = 0;
                System.out.println(Health1);
            }
            else if(damage>0)
            {
                System.out.println("Player 1 Has damaged Player 2 by "+(damage));
                Health2 = Health2 - damage;
                damage = 0;
            }else{System.out.println("Error");}
            if(Health1<=0)
            {
                System.out.println("Player 1 has reached 0 Health. Player 2 has won.");
            }
            else if(Health2<=0)
            {
                System.out.println("Player 2 has reached 0 Health. Player 1 has won.");
            }
        }
        else{System.out.println("Error");}
    }
	public static void main(String[] args) {
		AlphabetWar obj = new AlphabetWar(3);
		obj.play('w');
		obj.play('z');
	}
}
