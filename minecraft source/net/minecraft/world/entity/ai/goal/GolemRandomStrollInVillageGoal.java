/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GolemRandomStrollInVillageGoal
/*     */   extends RandomStrollGoal
/*     */ {
/*     */   private static final int POI_SECTION_SCAN_RADIUS = 2;
/*     */   private static final int VILLAGER_SCAN_RADIUS = 32;
/*     */   private static final int RANDOM_POS_XY_DISTANCE = 10;
/*     */   private static final int RANDOM_POS_Y_DISTANCE = 7;
/*     */   
/*  25 */   public GolemRandomStrollInVillageGoal(PathfinderMob mob, double speedModifier) { super(mob, speedModifier, 240, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPosition() {
/*     */     Vec3 target;
/*  31 */     float randomValue = (this.mob.level()).random.nextFloat();
/*  32 */     if ((this.mob.level()).random.nextFloat() < 0.3F) {
/*  33 */       return getPositionTowardsAnywhere();
/*     */     }
/*     */     
/*  36 */     if (randomValue < 0.7F) {
/*  37 */       target = getPositionTowardsVillagerWhoWantsGolem();
/*  38 */       if (target == null) {
/*  39 */         target = getPositionTowardsPoi();
/*     */       }
/*     */     } else {
/*  42 */       target = getPositionTowardsPoi();
/*  43 */       if (target == null) {
/*  44 */         target = getPositionTowardsVillagerWhoWantsGolem();
/*     */       }
/*     */     } 
/*     */     
/*  48 */     return (target == null) ? getPositionTowardsAnywhere() : target;
/*     */   }
/*     */ 
/*     */   
/*  52 */   private Vec3 getPositionTowardsAnywhere() { return LandRandomPos.getPos(this.mob, 10, 7); }
/*     */ 
/*     */   
/*     */   private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
/*  56 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  57 */     List<Villager> villagers = level.getEntities(EntityType.VILLAGER, this.mob.getBoundingBox().inflate(32.0D), this::doesVillagerWantGolem);
/*  58 */     if (villagers.isEmpty()) {
/*  59 */       return null;
/*     */     }
/*  61 */     Villager villager = (Villager)villagers.get((this.mob.level()).random.nextInt(villagers.size()));
/*  62 */     Vec3 targetPos = villager.position();
/*  63 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, targetPos);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsPoi() {
/*  67 */     SectionPos targetSection = getRandomVillageSection();
/*  68 */     if (targetSection == null) {
/*  69 */       return null;
/*     */     }
/*     */     
/*  72 */     BlockPos targetPos = getRandomPoiWithinSection(targetSection);
/*  73 */     if (targetPos == null)
/*     */     {
/*  75 */       return null;
/*     */     }
/*     */     
/*  78 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(targetPos));
/*     */   }
/*     */   
/*     */   private SectionPos getRandomVillageSection() {
/*  82 */     ServerLevel level = (ServerLevel)this.mob.level();
/*     */ 
/*     */ 
/*     */     
/*  86 */     List<SectionPos> villageSections = (List)SectionPos.cube(SectionPos.of(this.mob), 2).filter(sectionPos -> (level.sectionsToVillage(sectionPos) == 0)).collect(Collectors.toList());
/*     */     
/*  88 */     if (villageSections.isEmpty()) {
/*  89 */       return null;
/*     */     }
/*  91 */     return (SectionPos)villageSections.get(level.random.nextInt(villageSections.size()));
/*     */   }
/*     */   
/*     */   private BlockPos getRandomPoiWithinSection(SectionPos sectionPos) {
/*  95 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  96 */     PoiManager poiManager = level.getPoiManager();
/*     */ 
/*     */     
/*  99 */     List<BlockPos> pois = (List)poiManager.getInRange(poiType -> true, sectionPos.center(), 8, PoiManager.Occupancy.IS_OCCUPIED).map(PoiRecord::getPos).collect(Collectors.toList());
/*     */     
/* 101 */     if (pois.isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return (BlockPos)pois.get(level.random.nextInt(pois.size()));
/*     */   }
/*     */ 
/*     */   
/* 108 */   private boolean doesVillagerWantGolem(Villager villager) { return villager.wantsToSpawnGolem(this.mob.level().getGameTime()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\GolemRandomStrollInVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */