package cloudProtocol;

import global.TypeConversions;

public class MessageTypes {
	
	public MessageTypes () {
		
	}
	
	public class Challenge {
		private String status = "Status not received";
		private byte [] challengeResponse = {0x0};
		
		
		
		public String getStatus() {
			return status;
		}
		public void setStatus(int status) {
			
			switch (status) {
			case 0:
				this.status = "NO_ERROR";
				break;
			case 1:
				this.status = "GENERIC_ERROR";
				break;
			case 2:
				this.status = "INVALID_ARGUMENT";	
				break;
			case 3:
				this.status = "RESOURCE_UNAVAILABLE";	
				break;
			case 4:
				this.status = "REJECTED_ERROR";
				break;
			case 5:
				this.status = "USE_HSIM";
				break;
			default:
				this.status = "INVALID_STATUS_WAS_RECEIVED";
			}
			
		}
		public byte[] getChallengeResponse() {
			return challengeResponse;
		}
		public void setChallengeResponse(byte[] challengeResponse) {
			this.challengeResponse = challengeResponse;
		}
	}
	
	public class Session {

			private String status = "Status not received";
			private byte [] sessionId = {0x0};
			private String digest;
			private String UTCtime; 
//			UTCoffset = decodedTlv.get(TLV_TYPE[4]);
//			byte[] capabilitiesBitmask = decodedTlv.get(TLV_TYPE[5]);
//			byte[] PlugUpdateURL = decodedTlv.get(TLV_TYPE[6]);
//			byte[] ForcePlugUpdate = decodedTlv.get(TLV_TYPE[7]);
//			byte[] APNName = decodedTlv.get(TLV_TYPE[8]);
//			byte[] APN = decodedTlv.get(TLV_TYPE[9]);
//			
//		}
			
			public String getStatus() {
				return status;
			}
			public void setStatus(int statusId) {
				

				switch (statusId) {
				case 0:
					this.status = "NO_ERROR";
					break;
				case 1:
					this.status = "GENERIC_ERROR";
					break;
				case 2:
					this.status = "INVALID_ARGUMENT";	
					break;
				case 3:
					this.status = "RESOURCE_UNAVAILABLE";	
					break;
				case 4:
					this.status = "REJECTED_ERROR";
					break;
				case 5:
					this.status = "USE_HSIM";
					break;
				default:
					this.status = "INVALID_STATUS_WAS_RECEIVED";
				}
				
					
				
			}
			
			public byte [] getSessionId() {
				return sessionId;
			}
			
			public int getSessionIdAsInt() {
				return TypeConversions.hexStringToDecimalInt(TypeConversions.byteArrayToHexString(sessionId));
			}
			public void setSessionId(byte[] sessionId) {
				this.sessionId = sessionId;
			}
			public String getDigest() {
				return digest;
			}
			public void setDigest(String digest) {
				this.digest = digest;
			}
			public String getUTCtime() {
				return UTCtime;
			}
			public void setUTCtime(String uTCtime) {
				UTCtime = uTCtime;
			}
	
		
		
		
	}
	
	

}
