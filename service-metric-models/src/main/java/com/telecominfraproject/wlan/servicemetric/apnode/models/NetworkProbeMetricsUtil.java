package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author dtop
 *
 */
public class NetworkProbeMetricsUtil implements Serializable {

    private static final long serialVersionUID = 1768233186160740818L;

    /**
     * 
     * If a single VLAN doesn't work: we'll say this AP can't reach it's DHCP server
     * 
     * @param networkProbeMetrics
     * @return
     */
   public static boolean isDhcpReachable(List<NetworkProbeMetrics> networkProbeMetrics) 
   {
       
      if(networkProbeMetrics==null){
          return false;
      } 
      
      boolean reachable = true;
      
      for(NetworkProbeMetrics metrics : networkProbeMetrics)
      {
         if(metrics.getDhcpState() != StateUpDownError.enabled)
         {
            reachable = false;
         }
      }
      
      return reachable;
   }

   public static boolean isDhcpReachable(String vlan, List<NetworkProbeMetrics> networkProbeMetrics) {
       if(networkProbeMetrics==null || vlan == null){
           return false;
       } 
       boolean reachable = true;
       for(NetworkProbeMetrics metrics : networkProbeMetrics)
       {
          if(vlan.equalsIgnoreCase(metrics.getVlanIF()) && !StateUpDownError.enabled.equals(metrics.getDhcpState()))
          {
             reachable = false;
          }
       }
       
       return reachable; 
   }

   /** 
    * We'll average out the DHCP latency of each VLAN
    * 
    * @param networkProbeMetrics
    * @return
    */
  public static Long getAvgDHCPLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
  {
     if(networkProbeMetrics==null){
         return null;
     } 
      
     long runningLatency = 0;
     int count = 0;
     
     for(NetworkProbeMetrics metrics : networkProbeMetrics)
     {
        if(StateUpDownError.enabled.equals(metrics.getDhcpState())) {
            runningLatency += metrics.getDhcpLatencyMs();
            count++;
        }
     }
     
     return count != 0 ? runningLatency / count : null;
  }

  /** 
   * We'll get max out the DHCP latency of each VLAN
   * 
   * @param networkProbeMetrics
   * @return
   */
    public static Long getMaxDHCPLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
    {
       if(networkProbeMetrics==null){
           return null;
       } 
        
       long maxLatency = 0;
       boolean hasLatency = false;
       
       for(NetworkProbeMetrics metrics : networkProbeMetrics)
       {
          if (StateUpDownError.enabled.equals(metrics.getDhcpState()) && metrics.getDhcpLatencyMs()>maxLatency){
              maxLatency = metrics.getDhcpLatencyMs();
              hasLatency = true;
          }
       }
       
       return hasLatency?maxLatency:null;
    }   
   
    /** 
     * We'll get min out the DHCP latency of each VLAN
     * 
     * @param networkProbeMetrics
     * @return
     */
      public static Long getMinDHCPLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
      {
         if(networkProbeMetrics==null){
             return null;
         } 
          
         long minLatency = Long.MAX_VALUE;
         boolean hasLatency = false;
         
         for(NetworkProbeMetrics metrics : networkProbeMetrics)
         {
            if (StateUpDownError.enabled.equals(metrics.getDhcpState()) && metrics.getDhcpLatencyMs()<minLatency){
                minLatency = metrics.getDhcpLatencyMs(); 
                hasLatency = true;
            }
         }
         
         return hasLatency?minLatency:null;
      }
      
      /**
       * 
       * If a single VLAN doesn't work: we'll say this AP can't reach it's DNS server
       * 
       * @param networkProbeMetrics
       * @return
       */
     public static boolean isDnsReachable(List<NetworkProbeMetrics> networkProbeMetrics) 
     {
         
        if(networkProbeMetrics==null){
            return false;
        } 
        
        boolean reachable = true;
        
        for(NetworkProbeMetrics metrics : networkProbeMetrics)
        {
           if(metrics.getDnsState() != StateUpDownError.enabled)
           {
              reachable = false;
              break;
           }
        }
        
        return reachable;
     }

     public static boolean isDnsReachable(String vlan, List<NetworkProbeMetrics> networkProbeMetrics) {
         if(networkProbeMetrics==null || vlan == null){
             return false;
         } 
         boolean reachable = true;
         for(NetworkProbeMetrics metrics : networkProbeMetrics)
         {
            if(vlan.equalsIgnoreCase(metrics.getVlanIF()) && !StateUpDownError.enabled.equals(metrics.getDnsState()))
            {
               reachable = false;
            }
         }
         
         return reachable;
     } 

     /** 
      * We'll average out the DNS latency of each VLAN
      * 
      * @param networkProbeMetrics
      * @return
      */
    public static Long getAvgDNSLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
    {
       if(networkProbeMetrics==null || networkProbeMetrics.isEmpty()){
           return null;
       } 
        
       long runningLatency = 0;
       int count = 0;
       for(NetworkProbeMetrics metrics : networkProbeMetrics)
       {
      	 // Only use latency values if we know DHCP and DNS probes are working.
          if(StateUpDownError.enabled.equals(metrics.getDhcpState()) && StateUpDownError.enabled.equals(metrics.getDnsState())) {
        	  runningLatency += metrics.getDnsLatencyMs();
        	  count++;
          }
       }
       
       return count != 0 ? runningLatency / count : null;
    }

    public static Long getDNSLatency(String vlan, List<NetworkProbeMetrics> networkProbeMetrics) {
        if(networkProbeMetrics==null || networkProbeMetrics.isEmpty() || vlan==null){
            return null;
        } 
        long runningLatency = 0;
        int count = 0;
        for(NetworkProbeMetrics metrics : networkProbeMetrics)
        {
          // Only use latency values if we know DHCP and DNS probes are working.
            if(vlan.equalsIgnoreCase(metrics.getVlanIF()) && StateUpDownError.enabled.equals(metrics.getDnsState())) {
               runningLatency += metrics.getDnsLatencyMs();
               count++;
           }
        }
        
        return count != 0 ? runningLatency / count : null;
    }


    /** 
     * We'll get max out the DNS latency of each VLAN
     * 
     * @param networkProbeMetrics
     * @return
     */
      public static Long getMaxDNSLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
      {
         if(networkProbeMetrics==null || networkProbeMetrics.isEmpty()){
             return null;
         } 
          
         long maxLatency = 0;
         boolean hasLatency = false;
         
         for(NetworkProbeMetrics metrics : networkProbeMetrics)
         {
        	 // Only use latency values if we know DHCP and DNS are working.
            if (StateUpDownError.enabled.equals(metrics.getDhcpState()) 
                    && StateUpDownError.enabled.equals(metrics.getDnsState()) 
                    && metrics.getDnsLatencyMs()>maxLatency){
                maxLatency = metrics.getDnsLatencyMs(); 
                hasLatency = true;
            }
         }
         
         return hasLatency?maxLatency:null;
      }   
     
      /** 
       * We'll get min out the DNS latency of each VLAN
       * 
       * @param networkProbeMetrics
       * @return
       */
        public static Long getMinDNSLatency(List<NetworkProbeMetrics> networkProbeMetrics) 
        {
           if(networkProbeMetrics==null || networkProbeMetrics.isEmpty()){
               return null;
           } 
            
           long minLatency = Long.MAX_VALUE;
           boolean hasLatency = false;

           for(NetworkProbeMetrics metrics : networkProbeMetrics)
           {
        	   
        	   // Only use latency values if we know DHCP and DNS are working.
              if (StateUpDownError.enabled.equals(metrics.getDhcpState()) 
                      && StateUpDownError.enabled.equals(metrics.getDnsState()) 
                      && metrics.getDnsLatencyMs()<minLatency){
                  minLatency = metrics.getDnsLatencyMs(); 
                  hasLatency = true;
              }
           }
           
           return hasLatency?minLatency:null;
        }



}
