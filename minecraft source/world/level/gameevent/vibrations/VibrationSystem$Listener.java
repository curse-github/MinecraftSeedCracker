/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ClipBlockStateContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Listener
/*     */   implements GameEventListener
/*     */ {
/*     */   private final VibrationSystem system;
/*     */   
/* 227 */   public Listener(VibrationSystem system) { this.system = system; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public PositionSource getListenerSource() { return this.system.getVibrationUser().getPositionSource(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public int getListenerRadius() { return this.system.getVibrationUser().getListenerRadius(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 242 */     VibrationSystem.Data data = this.system.getVibrationData();
/* 243 */     VibrationSystem.User user = this.system.getVibrationUser();
/*     */ 
/*     */     
/* 246 */     if (data.getCurrentVibration() != null) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (!user.isValidVibration(event, context)) {
/* 251 */       return false;
/*     */     }
/*     */     
/* 254 */     Optional<Vec3> listenerSourcePos = user.getPositionSource().getPosition(level);
/*     */     
/* 256 */     if (listenerSourcePos.isEmpty()) {
/* 257 */       return false;
/*     */     }
/*     */     
/* 260 */     Vec3 destination = (Vec3)listenerSourcePos.get();
/*     */ 
/*     */     
/* 263 */     if (!user.canReceiveVibration(level, BlockPos.containing(sourcePosition), event, context)) {
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     if (isOccluded(level, sourcePosition, destination)) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     scheduleVibration(level, data, event, context, sourcePosition, destination);
/*     */     
/* 273 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 277 */   public void forceScheduleVibration(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin) { this.system.getVibrationUser().getPositionSource().getPosition(level).ifPresent(p -> scheduleVibration(level, this.system.getVibrationData(), event, context, origin, p)); }
/*     */ 
/*     */ 
/*     */   
/* 281 */   private void scheduleVibration(ServerLevel level, VibrationSystem.Data data, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin, Vec3 dest) { data.selectionStrategy.addCandidate(new VibrationInfo(event, (float)origin.distanceTo(dest), origin, context.sourceEntity()), level.getGameTime()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static float distanceBetweenInBlocks(BlockPos origin, BlockPos dest) { return (float)Math.sqrt(origin.distSqr(dest)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOccluded(Level level, Vec3 origin, Vec3 dest) {
/* 306 */     Vec3 from = new Vec3(Mth.floor(origin.x) + 0.5D, Mth.floor(origin.y) + 0.5D, Mth.floor(origin.z) + 0.5D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     Vec3 to = new Vec3(Mth.floor(dest.x) + 0.5D, Mth.floor(dest.y) + 0.5D, Mth.floor(dest.z) + 0.5D);
/*     */ 
/*     */     
/* 314 */     for (Direction direction : Direction.values()) {
/* 315 */       Vec3 nudgedSource = from.relative(direction, 9.999999747378752E-6D);
/* 316 */       if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, state -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
/* 317 */         return false;
/*     */       }
/*     */     } 
/* 320 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Listener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */