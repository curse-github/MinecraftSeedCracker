/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class WorkAtComposter
/*     */   extends WorkAtPoi
/*     */ {
/*  22 */   private static final List<Item> COMPOSTABLE_ITEMS = ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void useWorkstation(ServerLevel level, Villager body) {
/*  29 */     Optional<GlobalPos> jobSiteMemory = body.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/*  30 */     if (jobSiteMemory.isEmpty()) {
/*     */       return;
/*     */     }
/*  33 */     GlobalPos jobSitePos = (GlobalPos)jobSiteMemory.get();
/*  34 */     BlockState blockState = level.getBlockState(jobSitePos.pos());
/*     */     
/*  36 */     if (blockState.is(Blocks.COMPOSTER)) {
/*  37 */       makeBread(level, body);
/*     */       
/*  39 */       compostItems(level, body, jobSitePos, blockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void compostItems(ServerLevel level, Villager body, GlobalPos jobSitePos, BlockState blockState) {
/*  45 */     BlockPos pos = jobSitePos.pos();
/*  46 */     if (((Integer)blockState.getValue(ComposterBlock.LEVEL)).intValue() == 8) {
/*  47 */       blockState = ComposterBlock.extractProduce(body, blockState, level, pos);
/*     */     }
/*     */ 
/*     */     
/*  51 */     int totalItemsToUse = 20;
/*  52 */     int minStackSize = 10;
/*     */     
/*  54 */     int[] itemsSeenSoFar = new int[COMPOSTABLE_ITEMS.size()];
/*     */     
/*  56 */     SimpleContainer inventory = body.getInventory();
/*  57 */     int containerSize = inventory.getContainerSize();
/*     */     
/*  59 */     BlockState tempState = blockState;
/*     */     
/*  61 */     for (int i = containerSize - 1; i >= 0 && totalItemsToUse > 0; i--) {
/*  62 */       ItemStack itemStack = inventory.getItem(i);
/*  63 */       int itemIndex = COMPOSTABLE_ITEMS.indexOf(itemStack.getItem());
/*  64 */       if (itemIndex != -1) {
/*     */ 
/*     */ 
/*     */         
/*  68 */         int stackSize = itemStack.getCount();
/*  69 */         int totalItemCount = itemsSeenSoFar[itemIndex] + stackSize;
/*  70 */         itemsSeenSoFar[itemIndex] = totalItemCount;
/*     */         
/*  72 */         int itemsToUse = Math.min(Math.min(totalItemCount - 10, totalItemsToUse), stackSize);
/*  73 */         if (itemsToUse > 0) {
/*  74 */           totalItemsToUse -= itemsToUse;
/*  75 */           for (int j = 0; j < itemsToUse; j++) {
/*  76 */             tempState = ComposterBlock.insertItem(body, tempState, level, itemStack, pos);
/*  77 */             if (((Integer)tempState.getValue(ComposterBlock.LEVEL)).intValue() == 7) {
/*  78 */               spawnComposterFillEffects(level, blockState, pos, tempState);
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     spawnComposterFillEffects(level, blockState, pos, tempState);
/*     */   }
/*     */ 
/*     */   
/*  89 */   private void spawnComposterFillEffects(ServerLevel level, BlockState blockState, BlockPos pos, BlockState newState) { level.levelEvent(1500, pos, (newState != blockState) ? 1 : 0); }
/*     */ 
/*     */   
/*     */   private void makeBread(ServerLevel level, Villager body) {
/*  93 */     SimpleContainer inventory = body.getInventory();
/*  94 */     if (inventory.countItem(Items.BREAD) > 36) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     int howMuchWheatIHave = inventory.countItem(Items.WHEAT);
/*  99 */     int maxAmountOfBreadToMake = 3;
/* 100 */     int amountOfWheatNeededToCraftOneBread = 3;
/* 101 */     int howMuchBreadToMake = Math.min(3, howMuchWheatIHave / 3);
/* 102 */     if (howMuchBreadToMake == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     int howMuchWheatToUse = howMuchBreadToMake * 3;
/* 107 */     inventory.removeItemType(Items.WHEAT, howMuchWheatToUse);
/* 108 */     ItemStack breadICantCarry = inventory.addItem(new ItemStack(Items.BREAD, howMuchBreadToMake));
/* 109 */     if (!breadICantCarry.isEmpty())
/* 110 */       body.spawnAtLocation(level, breadICantCarry, 0.5F); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WorkAtComposter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */