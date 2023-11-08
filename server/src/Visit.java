public class Visit {
	
	private int VID;
	private int PID;
	private String PlaceName;
	private String DateTime;
	private double Timeframe;
	
	public Visit(int VID, int PID, String PlaceName, String DateTime, double Timeframe) {
		this.VID = VID;
		this.PID = PID;
		this.PlaceName = PlaceName;
		this.DateTime = DateTime;
		this.Timeframe = Timeframe;
	}
	
	public int getVID() {
		return this.VID;
	}
	
	public int getPID() {
		return this.PID;
	}
	
	public String getPlaceName() {
		return this.PlaceName;
	}
	
	public String getDateTime() {
		return this.DateTime;
	}
	
	public double getTimeframe() {
		return this.Timeframe;
	}
}
