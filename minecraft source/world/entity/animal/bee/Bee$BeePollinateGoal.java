/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Optional;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class BeePollinateGoal
/*      */   extends Bee.BaseBeeGoal
/*      */ {
/*      */   private static final int MIN_POLLINATION_TICKS = 400;
/*      */   private static final double ARRIVAL_THRESHOLD = 0.1D;
/*      */   private static final int POSITION_CHANGE_CHANCE = 25;
/*      */   private static final float SPEED_MODIFIER = 0.35F;
/*      */   private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
/*      */   private static final float HOVER_POS_OFFSET = 0.33333334F;
/*      */   private static final int FLOWER_SEARCH_RADIUS = 5;
/*      */   private int successfulPollinatingTicks;
/*      */   private int lastSoundPlayedTick;
/*      */   private boolean pollinating;
/*      */   private Vec3 hoverPos;
/*      */   private int pollinatingTicks;
/*      */   private static final int MAX_POLLINATING_TICKS = 600;
/* 1144 */   private Long2LongOpenHashMap unreachableFlowerCache = new Long2LongOpenHashMap();
/*      */   BeePollinateGoal() {
/* 1146 */     super(paramBee);
/* 1147 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeeUse() {
/* 1152 */     if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
/* 1153 */       return false;
/*      */     }
/*      */     
/* 1156 */     if (Bee.this.hasNectar()) {
/* 1157 */       return false;
/*      */     }
/* 1159 */     if (Bee.this.level().isRaining()) {
/* 1160 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1164 */     Optional<BlockPos> nearbyPos = findNearbyFlower();
/* 1165 */     if (nearbyPos.isPresent()) {
/* 1166 */       Bee.this.savedFlowerPos = (BlockPos)nearbyPos.get();
/*      */       
/* 1168 */       Bee.access$1800(Bee.this).moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1169 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1173 */     Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.access$1900(Bee.this), 20, 60);
/* 1174 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1179 */     if (!this.pollinating) {
/* 1180 */       return false;
/*      */     }
/* 1182 */     if (!Bee.this.hasSavedFlowerPos()) {
/* 1183 */       return false;
/*      */     }
/* 1185 */     if (Bee.this.level().isRaining()) {
/* 1186 */       return false;
/*      */     }
/* 1188 */     if (hasPollinatedLongEnough()) {
/* 1189 */       return (Bee.access$2000(Bee.this).nextFloat() < 0.2F);
/*      */     }
/* 1191 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 1195 */   private boolean hasPollinatedLongEnough() { return (this.successfulPollinatingTicks > 400); }
/*      */ 
/*      */ 
/*      */   
/* 1199 */   private boolean isPollinating() { return this.pollinating; }
/*      */ 
/*      */ 
/*      */   
/* 1203 */   private void stopPollinating() { this.pollinating = false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() {
/* 1208 */     this.successfulPollinatingTicks = 0;
/* 1209 */     this.pollinatingTicks = 0;
/* 1210 */     this.lastSoundPlayedTick = 0;
/* 1211 */     this.pollinating = true;
/* 1212 */     Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */   }
/*      */ 
/*      */   
/*      */   public void stop() {
/* 1217 */     if (hasPollinatedLongEnough()) {
/* 1218 */       Bee.this.setHasNectar(true);
/*      */     }
/* 1220 */     this.pollinating = false;
/* 1221 */     Bee.access$2100(Bee.this).stop();
/*      */     
/* 1223 */     Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1228 */   public boolean requiresUpdateEveryTick() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void tick() {
/* 1234 */     if (!Bee.this.hasSavedFlowerPos()) {
/*      */       return;
/*      */     }
/*      */     
/* 1238 */     this.pollinatingTicks++;
/* 1239 */     if (this.pollinatingTicks > 600) {
/*      */       
/* 1241 */       Bee.this.dropFlower();
/* 1242 */       this.pollinating = false;
/* 1243 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */       
/*      */       return;
/*      */     } 
/* 1247 */     Vec3 flowerPos = Vec3.atBottomCenterOf(Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/*      */     
/* 1249 */     if (flowerPos.distanceTo(Bee.this.position()) > 1.0D) {
/* 1250 */       this.hoverPos = flowerPos;
/* 1251 */       setWantedPos();
/*      */       
/*      */       return;
/*      */     } 
/* 1255 */     if (this.hoverPos == null) {
/* 1256 */       this.hoverPos = flowerPos;
/*      */     }
/*      */     
/* 1259 */     boolean arrivedAtHoverPos = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/* 1260 */     boolean shouldSetWantedPos = true;
/*      */     
/* 1262 */     if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
/*      */       
/* 1264 */       Bee.this.dropFlower();
/*      */       
/*      */       return;
/*      */     } 
/* 1268 */     if (arrivedAtHoverPos) {
/* 1269 */       boolean shouldChangeHoverPositions = (Bee.access$2200(Bee.this).nextInt(25) == 0);
/* 1270 */       if (shouldChangeHoverPositions) {
/* 1271 */         this.hoverPos = new Vec3(flowerPos.x() + getOffset(), flowerPos.y(), flowerPos.z() + getOffset());
/*      */         
/* 1273 */         Bee.access$2300(Bee.this).stop();
/*      */       } else {
/* 1275 */         shouldSetWantedPos = false;
/*      */       } 
/*      */       
/* 1278 */       Bee.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
/*      */     } 
/*      */     
/* 1281 */     if (shouldSetWantedPos) {
/* 1282 */       setWantedPos();
/*      */     }
/*      */     
/* 1285 */     this.successfulPollinatingTicks++;
/*      */     
/* 1287 */     if (Bee.access$2400(Bee.this).nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1288 */       this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1289 */       Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1294 */   private void setWantedPos() { Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D); }
/*      */ 
/*      */ 
/*      */   
/* 1298 */   private float getOffset() { return (Bee.access$2500(Bee.this).nextFloat() * 2.0F - 1.0F) * 0.33333334F; }
/*      */ 
/*      */   
/*      */   private Optional<BlockPos> findNearbyFlower() {
/* 1302 */     Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Bee.this.blockPosition(), 5, 5, 5);
/* 1303 */     Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
/*      */     
/* 1305 */     for (BlockPos pos : closestNearbyFlowers) {
/*      */       
/* 1307 */       long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Float.MIN_VALUE);
/* 1308 */       if (Bee.this.level().getGameTime() < unreachableUntilTime) {
/* 1309 */         tempCache.put(pos.asLong(), unreachableUntilTime);
/*      */         continue;
/*      */       } 
/* 1312 */       if (Bee.attractsBees(Bee.this.level().getBlockState(pos))) {
/* 1313 */         Path path = Bee.access$2600(Bee.this).createPath(pos, 1);
/* 1314 */         if (path != null && path.canReach()) {
/* 1315 */           return Optional.of(pos);
/*      */         }
/* 1317 */         tempCache.put(pos.asLong(), Bee.this.level().getGameTime() + 600L);
/*      */       } 
/*      */     } 
/*      */     
/* 1321 */     this.unreachableFlowerCache = tempCache;
/* 1322 */     return Optional.empty();
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\bee\Bee$BeePollinateGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */