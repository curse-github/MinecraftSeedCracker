/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CropBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UseBonemeal
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private static final int BONEMEALING_DURATION = 80;
/*     */   private long nextWorkCycleTime;
/*     */   private long lastBonemealingSession;
/*     */   private int timeWorkedSoFar;
/*  30 */   private Optional<BlockPos> cropPos = Optional.empty();
/*     */ 
/*     */   
/*  33 */   public UseBonemeal() { super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  41 */     if (body.tickCount % 10 != 0 || (this.lastBonemealingSession != 0L && this.lastBonemealingSession + 160L > body.tickCount)) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     if (body.getInventory().countItem(Items.BONE_MEAL) <= 0) {
/*  46 */       return false;
/*     */     }
/*  48 */     this.cropPos = pickNextTarget(level, body);
/*  49 */     return this.cropPos.isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  54 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return (this.timeWorkedSoFar < 80 && this.cropPos.isPresent()); }
/*     */ 
/*     */   
/*     */   private Optional<BlockPos> pickNextTarget(ServerLevel level, Villager body) {
/*  58 */     BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
/*  59 */     Optional<BlockPos> result = Optional.empty();
/*  60 */     int count = 0;
/*  61 */     for (int x = -1; x <= 1; x++) {
/*  62 */       for (int y = -1; y <= 1; y++) {
/*  63 */         for (int z = -1; z <= 1; z++) {
/*  64 */           mutPos.setWithOffset(body.blockPosition(), x, y, z);
/*  65 */           if (validPos(mutPos, level) && 
/*  66 */             level.random.nextInt(++count) == 0) {
/*  67 */             result = Optional.of(mutPos.immutable());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return result;
/*     */   }
/*     */   
/*     */   private boolean validPos(BlockPos blockPos, ServerLevel level) {
/*  78 */     BlockState state = level.getBlockState(blockPos);
/*  79 */     Block block = state.getBlock();
/*  80 */     return (block instanceof CropBlock && !((CropBlock)block).isMaxAge(state));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  85 */     setCurrentCropAsTarget(body);
/*     */     
/*  87 */     body.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
/*     */     
/*  89 */     this.nextWorkCycleTime = timestamp;
/*  90 */     this.timeWorkedSoFar = 0;
/*     */   }
/*     */   
/*     */   private void setCurrentCropAsTarget(Villager body) {
/*  94 */     this.cropPos.ifPresent(pos -> {
/*  95 */           BlockPosTracker cropPosWrapper = new BlockPosTracker(pos);
/*  96 */           body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, cropPosWrapper);
/*  97 */           body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(cropPosWrapper, 0.5F, 1));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 103 */     body.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 104 */     this.lastBonemealingSession = body.tickCount;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 109 */     BlockPos targetPos = (BlockPos)this.cropPos.get();
/* 110 */     if (timestamp < this.nextWorkCycleTime || !targetPos.closerToCenterThan(body.position(), 1.0D)) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     ItemStack bonemealStack = ItemStack.EMPTY;
/* 115 */     SimpleContainer inventory = body.getInventory();
/* 116 */     int containerSize = inventory.getContainerSize();
/* 117 */     for (int i = 0; i < containerSize; i++) {
/* 118 */       ItemStack item = inventory.getItem(i);
/* 119 */       if (item.is(Items.BONE_MEAL)) {
/* 120 */         bonemealStack = item;
/*     */         break;
/*     */       } 
/*     */     } 
/* 124 */     if (!bonemealStack.isEmpty() && BoneMealItem.growCrop(bonemealStack, level, targetPos)) {
/* 125 */       level.levelEvent(1505, targetPos, 15);
/*     */       
/* 127 */       this.cropPos = pickNextTarget(level, body);
/* 128 */       setCurrentCropAsTarget(body);
/* 129 */       this.nextWorkCycleTime = timestamp + 40L;
/*     */     } 
/*     */     
/* 132 */     this.timeWorkedSoFar++;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\UseBonemeal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */