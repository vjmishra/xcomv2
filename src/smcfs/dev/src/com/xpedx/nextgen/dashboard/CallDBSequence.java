package com.xpedx.nextgen.dashboard;

//Sterling imports
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.ycp.core.YCPContext;

/**
 * Class Name : CallDBSequence
 * ----------------------------------------------------------------
 * Description: This class can is invoked for getting Database 
 * sequence number.
 * -----------------------------------------------------------------
 * @author Chandan Tiwari
 * @version 1.0
 */
public class CallDBSequence {
	public static long getNextDBSequenceNo(YFSEnvironment env, String dbSeqName){
		return ((YCPContext)env).getNextDBSeqNo(dbSeqName);
	}
}
