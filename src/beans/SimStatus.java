package beans;

public class SimStatus {

	private String simName, plugId, forced, imsi;
	private int allowed, is3g, is2g, geo_location_id;
	private boolean usim,sim;
	public SimStatus(String simName, String plugId, String forced,
			 String imsi,int allowed, int is3g, int is2g, int geo_location_id) {
		super();
		this.simName = simName;
		this.plugId = plugId;
		this.forced = forced;
		this.imsi = imsi;
		this.allowed = allowed;
		this.is3g = is3g;
		this.is2g = is2g;	
		this.geo_location_id = geo_location_id;
		
		
		if (is2g == 1 && is3g == 0) {
			setSim(true);
			setUsim(false);
		} else {
			setSim(false);
			setUsim(true);
		}
		
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getGeo_location_id() {
		return geo_location_id;
	}

	public void setGeo_location_id(int geo_location_id) {
		this.geo_location_id = geo_location_id;
	}

	public SimStatus() {
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getPlugId() {
		return plugId;
	}

	public void setPlugId(String plugId) {
		this.plugId = plugId;
	}

	public String getForced() {
		return forced;
	}

	public void setForced(String forced) {
		this.forced = forced;
	}

	public int getAllowed() {
		return allowed;
	}

	public void setAllowed(int allowed) {
		this.allowed = allowed;
	}

	public int getIs3g() {
		return is3g;
	}

	public void setIs3g(int is3g) {
		this.is3g = is3g;
	}

	public int getIs2g() {
		return is2g;
	}

	public void setIs2g(int is2g) {
		this.is2g = is2g;
	}

	@Override
	public String toString() {
		return "SimStatus [simName=" + simName + ", plugId=" + plugId
				+ ", forced=" + forced + ", allowed=" + allowed + ", is3g="
				+ is3g + ", is2g=" + is2g + " isSim = "+sim+" isUsim = "+usim+"]";
	}

	public boolean isUsim() {
		return usim;
	}

	public void setUsim(boolean usim) {
		this.usim = usim;
	}

	public boolean isSim() {
		return sim;
	}

	public void setSim(boolean sim) {
		this.sim = sim;
	}

}
