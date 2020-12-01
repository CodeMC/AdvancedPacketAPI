package io.codemc.advancedpacketapi.handler;

import org.inventivetalent.reflection.resolver.FieldResolver;

public class UnknownWrappedPacket extends WrappedPacket {

	protected final FieldResolver fieldResolver;
	
	public UnknownWrappedPacket(Object packet) {
		super(packet);
		fieldResolver = new FieldResolver(packet.getClass());
	}

	/**
	 * Modify a value of the packet
	 *
	 * @param field Name of the field to modify
	 * @param value Value to be assigned to the field
	 */
	public void setPacketValue(String field, Object value) {
		try {
			fieldResolver.resolve(field).set(getPacket(), value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Modify a value of the packet (without throwing an exception)
	 *
	 * @param field Name of the field to modify
	 * @param value Value to be assigned to the field
	 */
	public void setPacketValueSilent(String field, Object value) {
		try {
			fieldResolver.resolve(field).set(getPacket(), value);
		} catch (Exception e) {
		}
	}

	/**
	 * Modify a value of the packet
	 *
	 * @param index field-index in the packet class
	 * @param value value to be assigned to the field
	 */
	public void setPacketValue(int index, Object value) {
		try {
			fieldResolver.resolveIndex(index).set(getPacket(), value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Modify a value of the packet (without throwing an exception)
	 *
	 * @param index field-index in the packet class
	 * @param value value to be assigned to the field
	 */
	public void setPacketValueSilent(int index, Object value) {
		try {
			fieldResolver.resolveIndex(index).set(getPacket(), value);
		} catch (Exception e) {
		}
	}

	/**
	 * Get a value of the packet
	 *
	 * @param field Name of the field
	 * @return current value of the field
	 */
	public Object getPacketValue(String field) {
		try {
			return fieldResolver.resolve(field).get(getPacket());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a value of the packet (without throwing an exception)
	 *
	 * @param field Name of the field
	 * @return current value of the field
	 */
	public Object getPacketValueSilent(String field) {
		try {
			return fieldResolver.resolve(field).get(getPacket());
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Get a value of the packet
	 *
	 * @param index field-index in the packet class
	 * @return value of the field
	 */
	public Object getPacketValue(int index) {
		try {
			return fieldResolver.resolveIndex(index).get(getPacket());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a value of the packet (without throwing an exception)
	 *
	 * @param index field-index in the packet class
	 * @return value of the field
	 */
	public Object getPacketValueSilent(int index) {
		try {
			return fieldResolver.resolveIndex(index).get(getPacket());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public FieldResolver getFieldResolver() {
		return fieldResolver;
	}

	@Override
	public String toString() {
		return "UnknownWrappedPacket [getPacket()=" + getPacket() + "]";
	}
	
}
