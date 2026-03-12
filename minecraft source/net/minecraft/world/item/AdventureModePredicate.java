/*     */ package net.minecraft.world.item;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AdventureModePredicate {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  28 */   public static final Codec<AdventureModePredicate> CODEC = ExtraCodecs.compactListCodec(BlockPredicate.CODEC, ExtraCodecs.nonEmptyList(BlockPredicate.CODEC.listOf()))
/*  29 */     .xmap(AdventureModePredicate::new, p -> p.predicates);
/*     */   
/*  31 */   public static final StreamCodec<RegistryFriendlyByteBuf, AdventureModePredicate> STREAM_CODEC = StreamCodec.composite(BlockPredicate.STREAM_CODEC
/*  32 */       .apply(ByteBufCodecs.list()), predicate -> predicate.predicates, AdventureModePredicate::new);
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final Component CAN_BREAK_HEADER = Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY);
/*  37 */   public static final Component CAN_PLACE_HEADER = Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY);
/*  38 */   private static final Component UNKNOWN_USE = Component.translatable("item.canUse.unknown").withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   private final List<BlockPredicate> predicates;
/*     */   
/*     */   private List<Component> cachedTooltip;
/*     */   
/*     */   private BlockInWorld lastCheckedBlock;
/*     */   
/*     */   private boolean lastResult;
/*     */   
/*     */   private boolean checksBlockEntity;
/*     */   
/*  50 */   public AdventureModePredicate(List<BlockPredicate> predicates) { this.predicates = predicates; }
/*     */ 
/*     */   
/*     */   private static boolean areSameBlocks(BlockInWorld blockInWorld, BlockInWorld cachedBlock, boolean checkBlockEntity) {
/*  54 */     if (cachedBlock == null || blockInWorld.getState() != cachedBlock.getState()) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (!checkBlockEntity) {
/*  58 */       return true;
/*     */     }
/*  60 */     if (blockInWorld.getEntity() == null && cachedBlock.getEntity() == null) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (blockInWorld.getEntity() == null || cachedBlock.getEntity() == null) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/*  68 */     try { RegistryAccess registryAccess = blockInWorld.getLevel().registryAccess();
/*     */       
/*  70 */       CompoundTag inWorldTag = saveBlockEntity(blockInWorld.getEntity(), registryAccess, reporter);
/*  71 */       CompoundTag cachedTag = saveBlockEntity(cachedBlock.getEntity(), registryAccess, reporter);
/*     */       
/*  73 */       boolean bool = Objects.equals(inWorldTag, cachedTag);
/*  74 */       reporter.close(); return bool; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  78 */      } private static CompoundTag saveBlockEntity(BlockEntity blockEntity, RegistryAccess registryAccess, ProblemReporter reporter) { TagValueOutput inWorldOutput = TagValueOutput.createWithContext(reporter.forChild(blockEntity.problemPath()), registryAccess);
/*  79 */     blockEntity.saveWithId(inWorldOutput);
/*  80 */     return inWorldOutput.buildResult(); }
/*     */ 
/*     */   
/*     */   public boolean test(BlockInWorld blockInWorld) {
/*  84 */     if (areSameBlocks(blockInWorld, this.lastCheckedBlock, this.checksBlockEntity)) {
/*  85 */       return this.lastResult;
/*     */     }
/*     */     
/*  88 */     this.lastCheckedBlock = blockInWorld;
/*  89 */     this.checksBlockEntity = false;
/*     */     
/*  91 */     for (BlockPredicate predicate : this.predicates) {
/*  92 */       if (predicate.matches(blockInWorld)) {
/*  93 */         this.checksBlockEntity |= predicate.requiresNbt();
/*  94 */         this.lastResult = true;
/*  95 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     this.lastResult = false;
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   private List<Component> tooltip() {
/* 104 */     if (this.cachedTooltip == null) {
/* 105 */       this.cachedTooltip = computeTooltip(this.predicates);
/*     */     }
/* 107 */     return this.cachedTooltip;
/*     */   }
/*     */ 
/*     */   
/* 111 */   public void addToTooltip(Consumer<Component> consumer) { tooltip().forEach(consumer); }
/*     */ 
/*     */   
/*     */   private static List<Component> computeTooltip(List<BlockPredicate> predicates) {
/* 115 */     for (BlockPredicate predicate : predicates) {
/*     */       
/* 117 */       if (predicate.blocks().isEmpty()) {
/* 118 */         return List.of(UNKNOWN_USE);
/*     */       }
/*     */     } 
/* 121 */     return predicates.stream()
/* 122 */       .flatMap(predicate -> ((HolderSet)predicate.blocks().orElseThrow()).stream())
/* 123 */       .distinct()
/* 124 */       .map(block -> ((Block)block.value()).getName().withStyle(ChatFormatting.DARK_GRAY))
/* 125 */       .toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 130 */     if (this == obj) {
/* 131 */       return true;
/*     */     }
/* 133 */     if (obj instanceof AdventureModePredicate) { AdventureModePredicate predicate = (AdventureModePredicate)obj;
/* 134 */       return this.predicates.equals(predicate.predicates); }
/*     */     
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public int hashCode() { return this.predicates.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public String toString() { return "AdventureModePredicate{predicates=" + String.valueOf(this.predicates) + "}"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\AdventureModePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */