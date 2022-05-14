package mrkacafirekcz.doggomod;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.PacketByteBuf;

public class TrackedDoggoData {
	
	public static final TrackedDataHandler<DoggoFeeling> DOGGO_FEELING = new TrackedDataHandler<DoggoFeeling>() {
		
		public void write(PacketByteBuf packetByteBuf, DoggoFeeling doggoFeeling) {
			packetByteBuf.writeEnumConstant(doggoFeeling);
		}

		public DoggoFeeling read(PacketByteBuf packetByteBuf) {
			return (DoggoFeeling) packetByteBuf.readEnumConstant(DoggoFeeling.class);
		}

		public DoggoFeeling copy(DoggoFeeling doggoFeeling) {
			return doggoFeeling;
		}
	};
	
	public static final TrackedDataHandler<DoggoAction> DOGGO_ACTION = new TrackedDataHandler<DoggoAction>() {
		
		public void write(PacketByteBuf packetByteBuf, DoggoAction doggoAction) {
			packetByteBuf.writeEnumConstant(doggoAction);
		}

		public DoggoAction read(PacketByteBuf packetByteBuf) {
			return (DoggoAction) packetByteBuf.readEnumConstant(DoggoAction.class);
		}

		public DoggoAction copy(DoggoAction doggoAction) {
			return doggoAction;
		}
	};
}
