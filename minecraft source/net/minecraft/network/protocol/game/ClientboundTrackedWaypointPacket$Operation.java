/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*    */ import net.minecraft.world.waypoints.WaypointManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ static enum Operation
/*    */ {
/* 73 */   TRACK(WaypointManager::trackWaypoint),
/* 74 */   UNTRACK(WaypointManager::untrackWaypoint),
/* 75 */   UPDATE(WaypointManager::updateWaypoint);
/*    */   
/*    */   private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
/*    */   
/*    */   static  {
/* 80 */     BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 81 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*    */   }
/*    */   public static final IntFunction<Operation> BY_ID; public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC;
/* 84 */   Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) { this.action = action; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTrackedWaypointPacket$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */