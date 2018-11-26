import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

class shamir{
	static int modlength;
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = 5;
		int k = 3;
		System.out.println("enter the first secret:");
		BigInteger secret1 = sc.nextBigInteger();
		System.out.println("enter the second secret:");
		BigInteger secret2 = sc.nextBigInteger();
		modlength = secret1.bitLength()+1;
		//System.out.println(modlength);
		
		BigInteger primenum = genPrime();
		System.out.println("Prime is:"+primenum);
		BigInteger[] coeff = new BigInteger[k-1];
		BigInteger[] participants = new BigInteger[k];

		for(int i=0;i<k-1;i++){
			coeff[i] = randomlim(primenum);
			System.out.println("a"+(i+1)+":"+coeff[i]);
		}

		//Shares
		
		BigInteger[] shares1 = secret_share(n,secret1,k,coeff,primenum);
		System.out.println("Shares of first secret:");
		for(int i=0;i<n;i++){
			System.out.println("share n"+(i+1)+":"+shares1[i]);
		}

		System.out.println();
		BigInteger[] shares2 = secret_share(n,secret2,k,coeff,primenum);
		System.out.println("Shares of second secret:");
		for(int i=0;i<n;i++){
			System.out.println("share n"+(i+1)+":"+shares2[i]);
		}

		//Share sum
		BigInteger[] share_sum = add_share(n,shares1,shares2,primenum);
		for(int i=0;i<n;i++){
			System.out.println("share sum is:"+share_sum[i]);
		}

		//Share reconstruction
		BigInteger secret_sum = new BigInteger("0");
		BigInteger[] participants_sharesum = new BigInteger[k];
		BigInteger[] participants_value = new BigInteger[k];
		for(int i=0;i<k;i++){
			System.out.println("enter the participants value and share:");
			participants_value[i]=sc.nextBigInteger();
			participants_sharesum[i]=sc.nextBigInteger();
			//System.out.println(participants_sharesum[i]);
		}
		for(int i=0;i<k;i++){
			double term=1;
				for(int j=0;j<k;j++){
					if(participants_value[i].intValue()!=participants_value[j].intValue()){
						BigInteger num = new BigInteger(Integer.toString(participants_value[j].intValue()*(-1)));
						BigInteger den = new BigInteger(Integer.toString(participants_value[i].intValue()-participants_value[j].intValue()));	
						term=term*(num.doubleValue())/(den.doubleValue());
					}
				}
				term=term*participants_sharesum[i].intValue();
				secret_sum = secret_sum.add(new BigInteger(Integer.toString((int)term)));
		}
		while(secret_sum.intValue()<0)
        	secret_sum = secret_sum.add(primenum);
        System.out.println("The secret is: "+secret_sum.mod(primenum));
		
	}

	static BigInteger[] secret_share(int n,BigInteger secret,int k,BigInteger[] coeff,BigInteger primenum){
		BigInteger[] shares = new BigInteger[n];
		for(int i=0;i<n;i++){
			BigInteger sum = secret;
			for(int j=0;j<k-1;j++){
				BigInteger power = new BigInteger(Integer.toString((int)(Math.pow((i+1),(j+1)))));
				//System.out.println("power:"+ power);
				sum = sum.add(coeff[j].multiply(power));
				//System.out.println("sum:"+sum);
			}
			shares[i] = sum.mod(primenum);
			//System.out.println("share n."+(i+1)+": "+shares[i]);
		}
		return shares;
	}

	static BigInteger[] add_share(int n,BigInteger[] share1,BigInteger[] share2,BigInteger primenum){
		BigInteger[] sum = new BigInteger[n];
		System.out.println();
		System.out.println("Secret sum is:");
		for(int i=0;i<n;i++){
			sum[i] = share1[i].add(share2[i]);
			sum[i] = sum[i].mod(primenum);
			//System.out.println("share sum:"+sum[i]);
		}
		return sum;
	}

	static BigInteger genPrime(){
		BigInteger b1;
		Random rnd1 = new Random();
		b1 = BigInteger.probablePrime(modlength,rnd1);
		return b1;
	}

	static BigInteger randomlim(BigInteger p){
		BigInteger b2;
		Random rnd2 = new Random();
		do{
			b2 = new BigInteger(modlength,rnd2);
		}while(b2.compareTo(BigInteger.ZERO) <0 || b2.compareTo(p) >= 0);
		return b2;
	}
}
