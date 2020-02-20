package net.improvedsurvival.mixin.server.entity;

import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(FurnaceMinecartEntity.class)
public class FurnaceMinecartEntityMixin {
	private final List<Chunk> loadedChunks = new ArrayList<>();
	
	@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo callbackInfo) {
		FurnaceMinecartEntity entity = (FurnaceMinecartEntity) (Object) this;
		ChunkPos inChunk = entity.world.getChunk(entity.getBlockPos()).getPos();
		for(Chunk c : loadedChunks)
			entity.world.getChunkManager().setChunkForced(c.getPos(), false);
		loadedChunks.clear();
		for(int x = inChunk.x - 1; x <= inChunk.x + 1; x++) {
			for(int z = inChunk.z - 1; z <= inChunk.z + 1; z++) {
				Chunk c = entity.world.getChunk(x, z);
				if(loadedChunks.contains(c))
					continue;
				entity.world.getChunkManager().setChunkForced(c.getPos(), true);
				loadedChunks.add(c);
			}
		}
	}
}