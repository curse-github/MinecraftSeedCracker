/*    */ package net.minecraft.nbt;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*    */ 
/*    */ public class TagParser<T> extends Object {
/* 14 */   public static final SimpleCommandExceptionType ERROR_TRAILING_DATA = new SimpleCommandExceptionType(Component.translatable("argument.nbt.trailing"));
/* 15 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_COMPOUND = new SimpleCommandExceptionType(Component.translatable("argument.nbt.expected.compound"));
/*    */   
/*    */   public static final char ELEMENT_SEPARATOR = ',';
/*    */   
/*    */   public static final char NAME_VALUE_SEPARATOR = ':';
/* 20 */   private static final TagParser<Tag> NBT_OPS_PARSER = create(NbtOps.INSTANCE);
/*    */ 
/*    */   
/* 23 */   public static final Codec<CompoundTag> FLATTENED_CODEC = Codec.STRING.comapFlatMap(s -> {
/*    */         try {
/* 25 */           Tag result = (Tag)NBT_OPS_PARSER.parseFully(s);
/* 26 */           if (result instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)result;
/* 27 */             return DataResult.success(compoundTag, Lifecycle.stable()); }
/*    */           
/* 29 */           return DataResult.error(());
/* 30 */         } catch (CommandSyntaxException e) {
/* 31 */           Objects.requireNonNull(e); return DataResult.error(e::getMessage);
/*    */         } 
/*    */       }CompoundTag::toString);
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final Codec<CompoundTag> LENIENT_CODEC = Codec.withAlternative(FLATTENED_CODEC, CompoundTag.CODEC);
/*    */ 
/*    */   
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   private final Grammar<T> grammar;
/*    */ 
/*    */   
/*    */   private TagParser(DynamicOps<T> ops, Grammar<T> grammar) {
/* 46 */     this.ops = ops;
/* 47 */     this.grammar = grammar;
/*    */   }
/*    */ 
/*    */   
/* 51 */   public DynamicOps<T> getOps() { return this.ops; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public static <T> TagParser<T> create(DynamicOps<T> ops) { return new TagParser(ops, SnbtGrammar.createParser(ops)); }
/*    */ 
/*    */   
/*    */   private static CompoundTag castToCompoundOrThrow(StringReader reader, Tag result) throws CommandSyntaxException {
/* 59 */     if (result instanceof CompoundTag) return (CompoundTag)result;
/*    */ 
/*    */     
/* 62 */     throw ERROR_EXPECTED_COMPOUND.createWithContext(reader);
/*    */   }
/*    */   
/*    */   public static CompoundTag parseCompoundFully(String input) throws CommandSyntaxException {
/* 66 */     StringReader reader = new StringReader(input);
/* 67 */     return castToCompoundOrThrow(reader, (Tag)NBT_OPS_PARSER.parseFully(reader));
/*    */   }
/*    */ 
/*    */   
/* 71 */   public T parseFully(String input) throws CommandSyntaxException { return (T)parseFully(new StringReader(input)); }
/*    */ 
/*    */   
/*    */   public T parseFully(StringReader reader) throws CommandSyntaxException {
/* 75 */     T result = (T)this.grammar.parseForCommands(reader);
/*    */     
/* 77 */     reader.skipWhitespace();
/*    */     
/* 79 */     if (reader.canRead()) {
/* 80 */       throw ERROR_TRAILING_DATA.createWithContext(reader);
/*    */     }
/* 82 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 86 */   public T parseAsArgument(StringReader reader) throws CommandSyntaxException { return (T)this.grammar.parseForCommands(reader); }
/*    */ 
/*    */   
/*    */   public static CompoundTag parseCompoundAsArgument(StringReader reader) throws CommandSyntaxException {
/* 90 */     Tag result = (Tag)NBT_OPS_PARSER.parseAsArgument(reader);
/* 91 */     return castToCompoundOrThrow(reader, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TagParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */