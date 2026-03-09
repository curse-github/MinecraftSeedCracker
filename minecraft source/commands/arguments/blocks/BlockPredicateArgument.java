/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockPredicateArgument
/*     */   extends Object
/*     */   implements ArgumentType<BlockPredicateArgument.Result> {
/*  31 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}" });
/*     */   
/*     */   private final HolderLookup<Block> blocks;
/*     */ 
/*     */   
/*  36 */   public BlockPredicateArgument(CommandBuildContext context) { this.blocks = context.lookupOrThrow(Registries.BLOCK); }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static BlockPredicateArgument blockPredicate(CommandBuildContext context) { return new BlockPredicateArgument(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public Result parse(StringReader reader) throws CommandSyntaxException { return parse(this.blocks, reader); }
/*     */ 
/*     */   
/*     */   public static Result parse(HolderLookup<Block> blocks, StringReader reader) throws CommandSyntaxException {
/*  49 */     return (Result)BlockStateParser.parseForTesting(blocks, reader, true).map(block -> 
/*  50 */         new BlockPredicate(block.blockState(), block.properties().keySet(), block.nbt()), tag -> 
/*  51 */         new TagPredicate(tag.tag(), tag.vagueProperties(), tag.nbt()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static Predicate<BlockInWorld> getBlockPredicate(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return (Predicate)context.getArgument(name, Result.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return BlockStateParser.fillSuggestions(this.blocks, builder, true, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */ 
/*     */   
/*     */   private static class BlockPredicate
/*     */     implements Result
/*     */   {
/*     */     private final BlockState state;
/*     */     
/*     */     private final Set<Property<?>> properties;
/*     */     
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public BlockPredicate(BlockState state, Set<Property<?>> properties, CompoundTag nbt) {
/*  79 */       this.state = state;
/*  80 */       this.properties = properties;
/*  81 */       this.nbt = nbt;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(BlockInWorld blockInWorld) {
/*  86 */       BlockState state = blockInWorld.getState();
/*     */       
/*  88 */       if (!state.is(this.state.getBlock())) {
/*  89 */         return false;
/*     */       }
/*     */       
/*  92 */       for (Property<?> property : this.properties) {
/*  93 */         if (state.getValue(property) != this.state.getValue(property)) {
/*  94 */           return false;
/*     */         }
/*     */       } 
/*     */       
/*  98 */       if (this.nbt != null) {
/*  99 */         BlockEntity entity = blockInWorld.getEntity();
/* 100 */         return (entity != null && NbtUtils.compareNbt(this.nbt, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true));
/*     */       } 
/*     */       
/* 103 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     public boolean requiresNbt() { return (this.nbt != null); }
/*     */   }
/*     */   
/*     */   private static class TagPredicate
/*     */     implements Result {
/*     */     private final HolderSet<Block> tag;
/*     */     private final CompoundTag nbt;
/*     */     private final Map<String, String> vagueProperties;
/*     */     
/*     */     private TagPredicate(HolderSet<Block> tag, Map<String, String> vagueProperties, CompoundTag nbt) {
/* 118 */       this.tag = tag;
/* 119 */       this.vagueProperties = vagueProperties;
/* 120 */       this.nbt = nbt;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(BlockInWorld blockInWorld) {
/* 125 */       BlockState state = blockInWorld.getState();
/*     */       
/* 127 */       if (!state.is(this.tag)) {
/* 128 */         return false;
/*     */       }
/*     */       
/* 131 */       for (Map.Entry<String, String> entry : this.vagueProperties.entrySet()) {
/* 132 */         Property<?> property = state.getBlock().getStateDefinition().getProperty((String)entry.getKey());
/* 133 */         if (property == null) {
/* 134 */           return false;
/*     */         }
/* 136 */         Comparable<?> value = (Comparable)property.getValue((String)entry.getValue()).orElse(null);
/* 137 */         if (value == null) {
/* 138 */           return false;
/*     */         }
/* 140 */         if (state.getValue(property) != value) {
/* 141 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 145 */       if (this.nbt != null) {
/* 146 */         BlockEntity entity = blockInWorld.getEntity();
/* 147 */         return (entity != null && NbtUtils.compareNbt(this.nbt, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true));
/*     */       } 
/*     */       
/* 150 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 155 */     public boolean requiresNbt() { return (this.nbt != null); }
/*     */   }
/*     */   
/*     */   public static interface Result extends Predicate<BlockInWorld> {
/*     */     boolean requiresNbt();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */