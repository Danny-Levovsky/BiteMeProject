package entites;


public class ClientDetails {
	
	private String ip;
	private String hostName;
	private boolean status;

	public ClientDetails(String hostName,String ip,boolean status) {
		this.ip = ip;
		this.hostName = hostName;
		this.status = status;

	}
	
	public String getIp() {
		return ip;
	}
	
	public String getHostName() {
		return hostName;
	}

	public boolean getStatus() {
		return status;
	}
	
	@Override
	public boolean equals(Object obj) {
		ClientDetails tmp =(ClientDetails) obj;
        if(tmp.getIp().equals(ip) && tmp.getHostName().equals(hostName))
        	return true;
        return false;
	}
		
}