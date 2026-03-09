/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BlockStateArgument
/*    */   extends Object implements ArgumentType<BlockInput> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}" });
/*    */   
/*    */   private final HolderLookup<Block> blocks;
/*    */ 
/*    */   
/* 25 */   public BlockStateArgument(CommandBuildContext context) { this.blocks = context.lookupOrThrow(Registries.BLOCK); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static BlockStateArgument block(CommandBuildContext context) { return new BlockStateArgument(context); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockInput parse(StringReader reader) throws CommandSyntaxException {
/* 34 */     BlockStateParser.BlockResult result = BlockStateParser.parseForBlock(this.blocks, reader, true);
/* 35 */     return new BlockInput(result.blockState(), result.properties().keySet(), result.nbt());
/*    */   }
/*    */ 
/*    */   
/* 39 */   public static BlockInput getBlock(CommandContext<CommandSourceStack> context, String name) { return (BlockInput)context.getArgument(name, BlockInput.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return BlockStateParser.fillSuggestions(this.blocks, builder, false, true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockStateArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */