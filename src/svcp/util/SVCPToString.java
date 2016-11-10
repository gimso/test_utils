package svcp.util;

import static global.Conversions.byteArrayToHexString;
import static global.Conversions.byteArraysToInt;
import static global.Conversions.stringASCIIFromByteArray;

import svcp.enums.AllowedModule;
import svcp.enums.ApplyUpdate;
import svcp.enums.Cloud3GConnection;
import svcp.enums.FileId;
import svcp.enums.FileType;
import svcp.enums.LogLevel;
import svcp.enums.METype;
import svcp.enums.MeModem4gOnline;
import svcp.enums.Mode;
import svcp.enums.PowerSupplyFromMe;
import svcp.enums.ResultTags;
import svcp.enums.FilePath;
import svcp.enums.SetFilesOptions;
import svcp.enums.SimGeneration;
import svcp.enums.Tag;
import svcp.enums.UICCRelay;
import svcp.enums.VsimType;

public class SVCPToString {

	public static String TagToString(Tag tag, byte[] value) {
		if (value.length < 1)
			return "EMPTY_VALUE";
		
		switch (tag) {
			// ASCII TLV's
			case FW_VERSION:
			case HW_VERSION:
			case CONFIGURATION_NAME:
			case LOG_LINE:
				return stringASCIIFromByteArray(value);
			// Binary TLV's
			case PACKET_PAYLOAD:
				return "(" + value.length + " bytes)";
			// Numbers (Hex value)
			case FILE_DATA:
			case AUTHENTICATION_DATA:
			case PHONE_NUMBER:
			case IMSI:
			case UPDATE_SIZE:
			case PACKET_BEGIN:
			case PACKET_SIZE:
			case APPLY_NOW:
			case NAA_INIT:
			case UICC_RESET:
			case BOYCOTT:
			case GET_ALL:
				return byteArrayToHexString(value);
			// default path / id on the vfs
			case FILE_PATH:
				return FilePath.getFilePath(value).getName();
			case FILE_ID:
				return FileId.getFileId(value).getName();		
			// ME type
			case ME_TYPE:
				return METype.getMEType(byteArraysToInt(value)).name();
			// Results
			case RESULT_TAG:
				return ResultTags.getResultTag(value[0]).name();
			// Mode
			case MODE:
				return Mode.getMode(byteArraysToInt(value)).name();
			// Apply Update
			case APPLY_UPDATE:
				return ApplyUpdate.getApplyUpdate(byteArraysToInt(value)).name();
			// UICC Source and Destination
			case UICC_RELAY:
				return uiccRelay(value);
			// File Type
			case FILE_TYPE:
				return FileType.getFileType(byteArraysToInt(value)).name();
			// Log Level TLV
			case LOG_LEVEL:
				return LogLevel.getLogLevel(byteArraysToInt(value)).name();
			// vSim type
			case VSIM_TYPE:
				return VsimType.getVsim(byteArraysToInt(value)).name();	
			// SIM generation 3g/4g
			case SIM_GENERATION:
				SimGeneration simGeneration = SimGeneration.getSimGeneration(byteArraysToInt(value));
				return simGeneration != null ? simGeneration.name() : String.format("%X", simGeneration);
			// Is power supply from me on/off
			case POWER_SUPPLY_FROM_ME:
				return PowerSupplyFromMe.getPowerSupplyMode(byteArraysToInt(value)).name();
			// Is power supply from me on/off
			case CLOUD_3G_CONNECTION:
				return Cloud3GConnection.getCloud3GConnection(byteArraysToInt(value)).name();
			case ME_MODEM_4G_ONLINE:
				return MeModem4gOnline.getMeModem4gOnline(byteArraysToInt(value)).name();
			case SET_FILES_OPTIONS:
				return SetFilesOptions.getSetFilesOptions(byteArraysToInt(value)).name();
			case ALLOWED_MODULES:
				return AllowedModule.getAllowedModules(value).toString();	
			default:
				break;
		}
		return new String(value);
	}

	/**
	 * @param value
	 * @return
	 */
	public static String uiccRelay(byte[] value) {
		if(value.length==1)
			return  " UICC relay value[]=" + UICCRelay.getUICCRelay(value[0]);
		else if (value[0] < 0 && value[1] > -1)
			return  " UICC relay value [0] < 0,  value[1]=" + UICCRelay.getUICCRelay(value[1]);
		else if (value[1] < 0 && value[0] > -1)
			return  " UICC relay value [1] < 0,  value[0]=" + UICCRelay.getUICCRelay(value[0]);
		else if (value[0] < 0 && value[1] < 0)
			return  " UICC relay value[0] and  value[1] is < 0";
		else	{
		UICCRelay uiccRelay1 = UICCRelay.getUICCRelay(value[0]);
		UICCRelay uiccRelay2 = UICCRelay.getUICCRelay(value[1]);
		String src =  uiccRelay1 != null 
				? 	uiccRelay1.name()
				:	String.format("%X",value[0]);
		String dest = uiccRelay2 != null
				?	uiccRelay2.name()
				:	String.format("%X",value[1]);
		return "Source: " +src+"\t,Destination: " +dest;
		}
	}

}
