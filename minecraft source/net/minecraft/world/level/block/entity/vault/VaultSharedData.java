/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class VaultSharedData {
/*    */   static final String TAG_NAME = "shared_data";
/*    */   private ItemStack displayItem;
/*    */   private Set<UUID> connectedPlayers;
/* 17 */   static Codec<VaultSharedData> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 18 */         ItemStack.lenientOptionalFieldOf("display_item").forGetter(()), UUIDUtil.CODEC_LINKED_SET
/* 19 */         .lenientOptionalFieldOf("connected_players", Set.of()).forGetter(()), Codec.DOUBLE
/* 20 */         .lenientOptionalFieldOf("connected_particles_range", Double.valueOf(VaultConfig.DEFAULT.deactivationRange())).forGetter(()))
/* 21 */       .apply(i, VaultSharedData::new)); private double connectedParticlesRange; boolean isDirty;
/*    */   VaultSharedData(ItemStack displayItem, Set<UUID> connectedPlayers, double connectedParticlesRange) {
/* 23 */     this.displayItem = ItemStack.EMPTY;
/* 24 */     this.connectedPlayers = new ObjectLinkedOpenHashSet();
/* 25 */     this.connectedParticlesRange = VaultConfig.DEFAULT.deactivationRange();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.displayItem = displayItem;
/* 31 */     this.connectedPlayers.addAll(connectedPlayers);
/* 32 */     this.connectedParticlesRange = connectedParticlesRange;
/*    */   }
/*    */   VaultSharedData() {
/*    */     this.displayItem = ItemStack.EMPTY;
/*    */     this.connectedPlayers = new ObjectLinkedOpenHashSet();
/*    */     this.connectedParticlesRange = VaultConfig.DEFAULT.deactivationRange();
/*    */   }
/* 39 */   public ItemStack getDisplayItem() { return this.displayItem; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public boolean hasDisplayItem() { return !this.displayItem.isEmpty(); }
/*    */ 
/*    */   
/*    */   public void setDisplayItem(ItemStack stack) {
/* 47 */     if (ItemStack.matches(this.displayItem, stack)) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     this.displayItem = stack.copy();
/* 52 */     markDirty();
/*    */   }
/*    */ 
/*    */   
/* 56 */   boolean hasConnectedPlayers() { return !this.connectedPlayers.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   Set<UUID> getConnectedPlayers() { return this.connectedPlayers; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   double connectedParticlesRange() { return this.connectedParticlesRange; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void updateConnectedPlayersWithinRange(ServerLevel serverLevel, BlockPos pos, VaultServerData serverData, VaultConfig config, double limit) {
/* 71 */     Set<UUID> currentConnectedPlayers = (Set)config.playerDetector().detect(serverLevel, config.entitySelector(), pos, limit, false).stream().filter(uuid -> !serverData.getRewardedPlayers().contains(uuid)).collect(Collectors.toSet());
/*    */     
/* 73 */     if (!this.connectedPlayers.equals(currentConnectedPlayers)) {
/* 74 */       this.connectedPlayers = currentConnectedPlayers;
/* 75 */       markDirty();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 80 */   private void markDirty() { this.isDirty = true; }
/*    */ 
/*    */   
/*    */   void set(VaultSharedData from) {
/* 84 */     this.displayItem = from.displayItem;
/* 85 */     this.connectedPlayers = from.connectedPlayers;
/* 86 */     this.connectedParticlesRange = from.connectedParticlesRange;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultSharedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */