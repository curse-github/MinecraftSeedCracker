/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class VaultServerData {
/*     */   static final String TAG_NAME = "server_data";
/*  21 */   static Codec<VaultServerData> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.CODEC_LINKED_SET
/*  22 */         .lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter(()), Codec.LONG
/*  23 */         .lenientOptionalFieldOf("state_updating_resumes_at", Long.valueOf(0L)).forGetter(()), ItemStack.CODEC
/*  24 */         .listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter(()), Codec.INT
/*  25 */         .lenientOptionalFieldOf("total_ejections_needed", Integer.valueOf(0)).forGetter(()))
/*  26 */       .apply(i, VaultServerData::new)); private static final int MAX_REWARD_PLAYERS = 128; private final Set<UUID> rewardedPlayers;
/*     */   private long stateUpdatingResumesAt;
/*     */   
/*     */   VaultServerData(Set<UUID> rewardedPlayers, long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
/*  30 */     this.rewardedPlayers = new ObjectLinkedOpenHashSet();
/*     */     
/*  32 */     this.itemsToEject = new ObjectArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.rewardedPlayers.addAll(rewardedPlayers);
/*  39 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  40 */     this.itemsToEject.addAll(itemsToEject);
/*  41 */     this.totalEjectionsNeeded = totalEjectionsNeeded;
/*     */   }
/*     */   private final List<ItemStack> itemsToEject; private long lastInsertFailTimestamp; private int totalEjectionsNeeded; boolean isDirty;
/*     */   VaultServerData() {
/*     */     this.rewardedPlayers = new ObjectLinkedOpenHashSet();
/*     */     this.itemsToEject = new ObjectArrayList();
/*     */   }
/*  48 */   void setLastInsertFailTimestamp(long lastInsertFailTimestamp) { this.lastInsertFailTimestamp = lastInsertFailTimestamp; }
/*     */ 
/*     */ 
/*     */   
/*  52 */   long getLastInsertFailTimestamp() { return this.lastInsertFailTimestamp; }
/*     */ 
/*     */ 
/*     */   
/*  56 */   Set<UUID> getRewardedPlayers() { return this.rewardedPlayers; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   boolean hasRewardedPlayer(Player player) { return this.rewardedPlayers.contains(player.getUUID()); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void addToRewardedPlayers(Player player) {
/*  65 */     this.rewardedPlayers.add(player.getUUID());
/*     */     
/*  67 */     if (this.rewardedPlayers.size() > 128) {
/*  68 */       Iterator<UUID> iterator = this.rewardedPlayers.iterator();
/*  69 */       if (iterator.hasNext()) {
/*  70 */         iterator.next();
/*  71 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     markChanged();
/*     */   }
/*     */ 
/*     */   
/*  79 */   long stateUpdatingResumesAt() { return this.stateUpdatingResumesAt; }
/*     */ 
/*     */   
/*     */   void pauseStateUpdatingUntil(long stateUpdatingResumesAt) {
/*  83 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  84 */     markChanged();
/*     */   }
/*     */ 
/*     */   
/*  88 */   List<ItemStack> getItemsToEject() { return this.itemsToEject; }
/*     */ 
/*     */   
/*     */   void markEjectionFinished() {
/*  92 */     this.totalEjectionsNeeded = 0;
/*  93 */     markChanged();
/*     */   }
/*     */   
/*     */   void setItemsToEject(List<ItemStack> newItemsToEject) {
/*  97 */     this.itemsToEject.clear();
/*  98 */     this.itemsToEject.addAll(newItemsToEject);
/*  99 */     this.totalEjectionsNeeded = this.itemsToEject.size();
/* 100 */     markChanged();
/*     */   }
/*     */   
/*     */   ItemStack getNextItemToEject() {
/* 104 */     if (this.itemsToEject.isEmpty()) {
/* 105 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 108 */     return (ItemStack)Objects.requireNonNullElse((ItemStack)this.itemsToEject.get(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   ItemStack popNextItemToEject() {
/* 112 */     if (this.itemsToEject.isEmpty()) {
/* 113 */       return ItemStack.EMPTY;
/*     */     }
/* 115 */     markChanged();
/*     */     
/* 117 */     return (ItemStack)Objects.requireNonNullElse((ItemStack)this.itemsToEject.remove(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   void set(VaultServerData from) {
/* 121 */     this.stateUpdatingResumesAt = from.stateUpdatingResumesAt();
/* 122 */     this.itemsToEject.clear();
/* 123 */     this.itemsToEject.addAll(from.itemsToEject);
/* 124 */     this.rewardedPlayers.clear();
/* 125 */     this.rewardedPlayers.addAll(from.rewardedPlayers);
/*     */   }
/*     */ 
/*     */   
/* 129 */   private void markChanged() { this.isDirty = true; }
/*     */ 
/*     */   
/*     */   public float ejectionProgress() {
/* 133 */     if (this.totalEjectionsNeeded == 1) {
/* 134 */       return 1.0F;
/*     */     }
/*     */     
/* 137 */     return 1.0F - Mth.inverseLerp(getItemsToEject().size(), 1.0F, this.totalEjectionsNeeded);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultServerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */