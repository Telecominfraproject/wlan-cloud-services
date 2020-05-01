package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum ManagementRate 
{
   auto(0L),
   rate1mbps(1L),
   rate2mbps(2L),
   rate5dot5mbps(5L),
   rate6mbps(6L),
   rate9mbps(9L),
   rate11mbps(11L),
   rate12mbps(12L),
   rate18mbps(18L),
   rate24mbps(24L),
   
   UNSUPPORTED(-1L);

   private final long id;
   private static final Map<Long, ManagementRate> ELEMENTS = new HashMap<>();

   private ManagementRate(long id) {
      this.id = id;
   }

   public long getId() {
      return this.id;
   }

   public static ManagementRate getById(long enumId) {
      if (ELEMENTS.isEmpty()) {
         synchronized (ELEMENTS) {
            if (ELEMENTS.isEmpty()) {
               //initialize elements map
               for(ManagementRate met : ManagementRate.values()) {
                  ELEMENTS.put(met.getId(), met);
               }
            }
         }
      }
      return ELEMENTS.get(enumId);
   }
   
   @JsonCreator
   public static ManagementRate getByName(String value) {
       return JsonDeserializationUtils.deserializEnum(value, ManagementRate.class, UNSUPPORTED);
   }
   
   public static boolean isUnsupported(ManagementRate value) {
       return UNSUPPORTED.equals(value);
   }
}
