package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.LEDColour;

public class CEGWBlinkRequest extends EquipmentCommand {

		private static final long serialVersionUID = 3464950479960821571L;
	
		private boolean blinkAllLEDs;
		private int numCycles;
	    private int colour1DurationMs;
	    private int colour2DurationMs;
	    private LEDColour colour1;
	    private LEDColour colour2;

	    /**
	     * Constructor
	     * 
	     * @param inventoryId
	     * @param equipmentId
	     */
	    public CEGWBlinkRequest(String inventoryId, long equipmentId) {
	        super(CEGWCommandType.BlinkRequest, inventoryId, equipmentId);
	    }

	    /**
	     * Constructor used by JSON
	     */
	    public CEGWBlinkRequest() {
	        super(CEGWCommandType.BlinkRequest, null, 0);
	    }
	    
	    public boolean getBlinkAllLEDs() {
	        return this.blinkAllLEDs;
	    }
	    
	    public void setBlinkAllLEDs(boolean blinkAllLEDs) {
	        this.blinkAllLEDs = blinkAllLEDs;
	    }

	    public int getNumCycles() {
	        return numCycles;
	    }

	    public void setNumCycles(int numCycles) {
	        this.numCycles = numCycles;
	    }

	    public int getColour1DurationMs() {
	        return colour1DurationMs;
	    }

	    public void setColour1DurationMs(int colourDurationMs) {
	        this.colour1DurationMs = colourDurationMs;
	    }

	    public int getColour2DurationMs() {
	        return colour2DurationMs;
	    }

	    public void setColour2DurationMs(int colourDurationMs) {
	        this.colour2DurationMs = colourDurationMs;
	    }

	    public LEDColour getColour1() {
	        return colour1;
	    }

	    public void setColour1(LEDColour colour1) {
	        this.colour1 = colour1;
	    }

	    public LEDColour getColour2() {
	        return colour2;
	    }

	    public void setColour2(LEDColour colour2) {
	        this.colour2 = colour2;
	    }
	    
	    @Override
	    public boolean hasUnsupportedValue() {
	        if (super.hasUnsupportedValue()) {
	            return true;
	        }
	        if (LEDColour.isUnsupported(colour1) || LEDColour.isUnsupported(colour2)) {
	            return true;
	        }
	        return false;
	    }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(blinkAllLEDs, colour1, colour1DurationMs, colour2, colour2DurationMs, numCycles);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            CEGWBlinkRequest other = (CEGWBlinkRequest) obj;
            return blinkAllLEDs == other.blinkAllLEDs && colour1 == other.colour1 && colour1DurationMs == other.colour1DurationMs && colour2 == other.colour2
                    && colour2DurationMs == other.colour2DurationMs && numCycles == other.numCycles;
        }
}
