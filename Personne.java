class Personne {
private int cin;
private String nom;
private String prenom;
Personne(int cin , String nom, String prenom){
	this.cin=cin;
	this.nom=nom;
	this.prenom=prenom;
}
String getNom(){
	return this.nom;
}
String getPrenom(){
	return this.prenom;
}
int getCin() {
	return this.cin;
}
void setNom(String n){
	this.nom=n;
}
void setPrenom(String p){
	this.prenom=p;
}
void setCin(int c){
	this.cin=c;
}
public String toString() {
	return "Cin :"+this.getCin()+" nom: "+this.getNom()+" prenom: "+this.getPrenom();
}
}
abstract class Propriete{
	protected int id;
	protected Personne responsable;
	protected String adresse;
	protected double surface;
	Propriete(int id,Personne responsable, String adresse, double surface ){
		this.id=id;
		this.responsable=responsable;
		this.adresse=adresse;
		this.surface=surface;
	}
	int getId() {
		return this.id;
	}
	String getAdresse(){
		return this.adresse;
	}
	double getSurface(){
		return this.surface;
	}
	Personne getResponsable(){
		return this.responsable;
	}
	public String toString() {
		return "id: "+this.getId()+" responsable :"+this.getResponsable()+" adresse : "+this.getAdresse()+" surface: "+this.surface;
	}
	abstract public double calculImpot();
	public boolean equals(Object o) {
		if (o==null ||getClass() != o.getClass())
			return false;
		Propriete other = (Propriete) o;
		return this.id==other.id;
	}
}
class ProprietePrivee extends Propriete {
	protected int nbPieces;
	public int getNbPieces() {
		return nbPieces;
	}
	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}
	ProprietePrivee(int id,Personne responsable, String adresse, double surface,int nbPieces){
		super(id,responsable, adresse, surface);
		this.nbPieces=nbPieces;
	}
	public String toString() {
		return super.toString()+"nombre de pieces :"+this.getNbPieces();
	}
	@Override
	public double calculImpot() {
		return (super.getSurface()*(50/100))+10*this.getNbPieces();
	}
}
class ProprieteProfessionnelle extends Propriete {
	private int nbEmployes;
	private boolean estEtatique;
	public int getNbEmployes() {
		return nbEmployes;
	}
	public void setNbEmployes(int nbEmployes) {
		this.nbEmployes = nbEmployes;
	}
	public boolean isEstEtatique() {
		return estEtatique;
	}
	public void setEstEtatique(boolean estEtatique) {
		this.estEtatique = estEtatique;
	}
	ProprieteProfessionnelle(int id,Personne responsable, String adresse, double surface,int nbEmployes,boolean estEtatique){
		super(id,responsable, adresse, surface);
		this.nbEmployes=nbEmployes;
		this.estEtatique=estEtatique;
	}
	@Override
	public double calculImpot() {
		if(!isEstEtatique()) {
			return super.getSurface()+30*this.getNbEmployes();
		}
		else
			return 0;
	}
}
class Villa extends ProprietePrivee {
	private boolean avecPiscine;
	public boolean getAvecPiscine() {
		return avecPiscine;
	}
	public void setAvecPiscine(boolean avecPiscine) {
		this.avecPiscine = avecPiscine;
	}
	Villa(int id,Personne responsable, String adresse, double surface,int nbPieces,boolean avecPiscine){
		super(id,responsable, adresse,  surface,nbPieces);
		this.avecPiscine=avecPiscine;
	}
	public String toString() {
		return super.toString()+" Piscine :"+this.getAvecPiscine();
	}
	@Override
	public double calculImpot() {
		if(getAvecPiscine())
			return super.calculImpot()+200;
		else 
			return super.calculImpot();
	}
}
class Appartement extends ProprietePrivee{
	private int numEtage;
	public int getNumEtage() {
		return numEtage;
	}
	public void setNumEtage(int numEtage) {
		this.numEtage = numEtage;
	}
	Appartement(int id,Personne responsable, String adresse, double surface,int nbPieces,int numEtage){
		super(id,responsable, adresse,  surface,nbPieces);
		this.numEtage=numEtage;
	}
	public String toString() {
		return super.toString()+" Piscine :"+this.getNumEtage();
	}
}
interface GestionPropriete {
	public final int MAX_PROPRIETES=50;
	void afficherProprietes();
	boolean ajouter(Propriete p);
	boolean supprimer(Propriete p);
	
}
class Lotissement implements GestionPropriete {
	protected Propriete [] tabProp;
	protected int nombre;
	protected int capacite;
	Lotissement(int capacite){
		if(capacite <= MAX_PROPRIETES) {
			this.capacite=capacite;
			tabProp=new Propriete[capacite];
			nombre=0;
		}
	}
Propriete getproprieteByIndice (int i) {
		if(i>0 && i< nombre) 
			return tabProp[i];
		else
			return null;
	}
int getnbPièces() {
	int n=0;
	for(int i=0;i<nombre;i++) {
		if(tabProp[i] instanceof ProprietePrivee) {
			n+=((ProprietePrivee)tabProp[i]).getNbPieces();
		}
			
	}
	return n;
}
public void afficherProprietes() {
	for(int i=0;i<nombre;i++) {
		System.out.println(tabProp[i].toString());
	}
}
public boolean ajouter(Propriete p) {
	if(nombre<capacite) {
		tabProp[nombre]=p;
		nombre++;
		return true;}
	else
		return false;
}
public boolean supprimer(Propriete p) {
	int i=0;
	boolean trouve=false;
	while(i<nombre && trouve ==false) {
		if(tabProp[i].equals(p)) {
			for(int j=i;j<nombre;j++) {
				tabProp[j]=tabProp[j+1];}
		    nombre--;
			trouve=true;
		}
		i++;
	}
	return trouve;

}
public void leMoins(){
	Propriete p=null;
	for(int i=0;i<nombre;i++) {
		if(tabProp[i] instanceof  ProprietePrivee ) {
			if(p==null) {
				p= tabProp[i];
			}	
		else if(p.calculImpot()<tabProp[i].calculImpot()) {
			p=tabProp[i];
		}
	}
}
	System.out.println(p.toString()+"le monatnt a payé = "+p.calculImpot());
}
}
class LotissementPrivee extends Lotissement{
	LotissementPrivee(int capacite){
		super(capacite);
	}
	public boolean ajouter(Propriete p) {
		if(nombre<capacite && p instanceof ProprietePrivee) {
			tabProp[nombre]=p;
			nombre++;
			return true;}
		else
			return false;
	}
	public ProprietePrivee getproprieteByIndice (int i) {
		return (ProprietePrivee)super.getproprieteByIndice(i);
	}
	public int getnbPieces() {
		return super.getnbPièces();
}
}
class Fiscalite{
	public static void main(String[] args) {
		Personne p1=new Personne (123 ,"mohamed","abidi");
		Personne p2=new Personne (187 ,"yasmine","gannouni");
		Personne p3=new Personne (136 ,"maryem","mahfoudh");
		Lotissement lot=new Lotissement(10);
		ProprietePrivee pr=new ProprietePrivee(1,p1,"Corniche",350,4);
		Villa v=new Villa(2,p2,"Dar Chaabane", 400,6,true);
		Appartement a=new Appartement(3,p2,"Hammamet",1200,8, 3); 
		ProprieteProfessionnelle pr1= new ProprieteProfessionnelle(4,p3,"Korba", 1000, 50,true);
		ProprieteProfessionnelle pr2=new ProprieteProfessionnelle(5,p1,"Bir Bouragba",2500, 400, false);
		lot.ajouter(pr);
		lot.ajouter(v);
		lot.ajouter(a);
		lot.ajouter(pr1);
		lot.ajouter(pr2);
		lot.afficherProprietes();
		System.out.println("le nombre des pieces :"+lot.getnbPièces()); 
		if(lot.supprimer(a))
			lot.afficherProprietes();
		System.out.println("lotissement privee :");
		Lotissement lt= new LotissementPrivee(10);
		lt.ajouter(pr);
		lt.ajouter(v);
		lt.ajouter(a);
		lt.ajouter(pr1);
		lt.ajouter(pr2);
		lt.afficherProprietes();
		System.out.println("le nombre des pieces :"+lot.getnbPièces()); 
		if(lt.supprimer(a))
			lt.afficherProprietes();
	}
}