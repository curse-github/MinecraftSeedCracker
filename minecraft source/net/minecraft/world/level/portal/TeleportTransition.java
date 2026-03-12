/*     */ package net.minecraft.world.level.portal;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class TeleportTransition extends Record {
/*     */   private final ServerLevel newLevel;
/*     */   private final Vec3 position;
/*     */   private final Vec3 deltaMovement;
/*     */   private final float yRot;
/*     */   
/*  15 */   public TeleportTransition(ServerLevel newLevel, Vec3 position, Vec3 deltaMovement, float yRot, float xRot, boolean missingRespawnBlock, boolean asPassenger, Set<Relative> relatives, PostTeleportTransition postTeleportTransition) { this.newLevel = newLevel; this.position = position; this.deltaMovement = deltaMovement; this.yRot = yRot; this.xRot = xRot; this.missingRespawnBlock = missingRespawnBlock; this.asPassenger = asPassenger; this.relatives = relatives; this.postTeleportTransition = postTeleportTransition; } private final float xRot; private final boolean missingRespawnBlock; private final boolean asPassenger; private final Set<Relative> relatives; private final PostTeleportTransition postTeleportTransition; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/portal/TeleportTransition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/portal/TeleportTransition; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/portal/TeleportTransition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/portal/TeleportTransition; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/portal/TeleportTransition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/portal/TeleportTransition;
/*  15 */     //   0	8	1	o	Ljava/lang/Object; } public ServerLevel newLevel() { return this.newLevel; } public Vec3 position() { return this.position; } public Vec3 deltaMovement() { return this.deltaMovement; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; } public boolean missingRespawnBlock() { return this.missingRespawnBlock; } public boolean asPassenger() { return this.asPassenger; } public Set<Relative> relatives() { return this.relatives; } public PostTeleportTransition postTeleportTransition() { return this.postTeleportTransition; }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PostTeleportTransition {
/*     */     void onTransition(Entity param1Entity);
/*     */     
/*     */     default PostTeleportTransition then(PostTeleportTransition postTeleportTransition) {
/*  22 */       return entity -> {
/*  23 */           onTransition(entity);
/*  24 */           postTeleportTransition.onTransition(entity);
/*     */         };
/*     */     } }
/*     */   public static final PostTeleportTransition DO_NOTHING = entity -> {
/*     */     
/*     */     };
/*  30 */   public static final PostTeleportTransition PLAY_PORTAL_SOUND = TeleportTransition::playPortalSound;
/*  31 */   public static final PostTeleportTransition PLACE_PORTAL_TICKET = TeleportTransition::placePortalTicket;
/*     */   
/*     */   private static void playPortalSound(Entity entity) {
/*  34 */     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/*  35 */       player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*  40 */   private static void placePortalTicket(Entity entity) { entity.placePortalTicket(BlockPos.containing(entity.position())); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public TeleportTransition(ServerLevel newLevel, Vec3 pos, Vec3 speed, float yRot, float xRot, PostTeleportTransition postTeleportTransition) { this(newLevel, pos, speed, yRot, xRot, Set.of(), postTeleportTransition); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public TeleportTransition(ServerLevel newLevel, Vec3 pos, Vec3 speed, float yRot, float xRot, Set<Relative> relatives, PostTeleportTransition postTeleportTransition) { this(newLevel, pos, speed, yRot, xRot, false, false, relatives, postTeleportTransition); }
/*     */ 
/*     */   
/*     */   public static TeleportTransition createDefault(ServerPlayer player, PostTeleportTransition postTeleportTransition) {
/*  52 */     ServerLevel newLevel = player.level().getServer().findRespawnDimension();
/*  53 */     LevelData.RespawnData respawnData = newLevel.getRespawnData();
/*  54 */     return new TeleportTransition(newLevel, findAdjustedSharedSpawnPos(newLevel, player), Vec3.ZERO, respawnData.yaw(), respawnData.pitch(), false, false, Set.of(), postTeleportTransition);
/*     */   }
/*     */   
/*     */   public static TeleportTransition missingRespawnBlock(ServerPlayer player, PostTeleportTransition postTeleportTransition) {
/*  58 */     ServerLevel newLevel = player.level().getServer().findRespawnDimension();
/*  59 */     LevelData.RespawnData respawnData = newLevel.getRespawnData();
/*  60 */     return new TeleportTransition(newLevel, findAdjustedSharedSpawnPos(newLevel, player), Vec3.ZERO, respawnData.yaw(), respawnData.pitch(), true, false, Set.of(), postTeleportTransition);
/*     */   }
/*     */ 
/*     */   
/*  64 */   private static Vec3 findAdjustedSharedSpawnPos(ServerLevel newLevel, Entity entity) { return entity.adjustSpawnLocation(newLevel, newLevel.getRespawnData().pos()).getBottomCenter(); }
/*     */ 
/*     */   
/*     */   public TeleportTransition withRotation(float yRot, float xRot) {
/*  68 */     return new TeleportTransition(
/*  69 */         newLevel(), 
/*  70 */         position(), 
/*  71 */         deltaMovement(), yRot, xRot, 
/*     */ 
/*     */         
/*  74 */         missingRespawnBlock(), 
/*  75 */         asPassenger(), 
/*  76 */         relatives(), 
/*  77 */         postTeleportTransition());
/*     */   }
/*     */   
/*     */   public TeleportTransition withPosition(Vec3 position) {
/*  81 */     return new TeleportTransition(
/*  82 */         newLevel(), position, 
/*     */         
/*  84 */         deltaMovement(), 
/*  85 */         yRot(), 
/*  86 */         xRot(), 
/*  87 */         missingRespawnBlock(), 
/*  88 */         asPassenger(), 
/*  89 */         relatives(), 
/*  90 */         postTeleportTransition());
/*     */   }
/*     */   
/*     */   public TeleportTransition transitionAsPassenger() {
/*  94 */     return new TeleportTransition(
/*  95 */         newLevel(), 
/*  96 */         position(), 
/*  97 */         deltaMovement(), 
/*  98 */         yRot(), 
/*  99 */         xRot(), 
/* 100 */         missingRespawnBlock(), true, 
/*     */         
/* 102 */         relatives(), 
/* 103 */         postTeleportTransition());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\portal\TeleportTransition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */