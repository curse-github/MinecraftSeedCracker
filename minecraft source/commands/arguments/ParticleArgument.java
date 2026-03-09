/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class ParticleArgument extends Object implements ArgumentType<ParticleOptions> {
/* 29 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "particle{foo:bar}" });
/* 30 */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(value -> Component.translatableEscape("particle.notFound", new Object[] { value }));
/* 31 */   public static final DynamicCommandExceptionType ERROR_INVALID_OPTIONS = new DynamicCommandExceptionType(message -> Component.translatableEscape("particle.invalidOptions", new Object[] { message }));
/*    */   
/*    */   private final HolderLookup.Provider registries;
/*    */   
/* 35 */   private static final TagParser<?> VALUE_PARSER = TagParser.create(NbtOps.INSTANCE);
/*    */ 
/*    */   
/* 38 */   public ParticleArgument(CommandBuildContext context) { this.registries = context; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static ParticleArgument particle(CommandBuildContext context) { return new ParticleArgument(context); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static ParticleOptions getParticle(CommandContext<CommandSourceStack> context, String name) { return (ParticleOptions)context.getArgument(name, ParticleOptions.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public ParticleOptions parse(StringReader reader) throws CommandSyntaxException { return readParticle(reader, this.registries); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ 
/*    */   
/*    */   public static ParticleOptions readParticle(StringReader reader, HolderLookup.Provider registries) throws CommandSyntaxException {
/* 60 */     ParticleType<?> type = readParticleType(reader, registries.lookupOrThrow(Registries.PARTICLE_TYPE));
/* 61 */     return readParticle(VALUE_PARSER, reader, type, registries);
/*    */   }
/*    */   
/*    */   private static ParticleType<?> readParticleType(StringReader reader, HolderLookup<ParticleType<?>> particles) throws CommandSyntaxException {
/* 65 */     Identifier id = Identifier.read(reader);
/* 66 */     ResourceKey<ParticleType<?>> key = ResourceKey.create(Registries.PARTICLE_TYPE, id);
/* 67 */     return (ParticleType)((Holder.Reference)particles.get(key).orElseThrow(() -> ERROR_UNKNOWN_PARTICLE.createWithContext(reader, id))).value();
/*    */   }
/*    */   
/*    */   private static <T extends ParticleOptions, O> T readParticle(TagParser<O> parser, StringReader reader, ParticleType<T> type, HolderLookup.Provider registries) throws CommandSyntaxException {
/*    */     O extraData;
/* 72 */     RegistryOps<O> ops = registries.createSerializationContext(parser.getOps());
/* 73 */     if (reader.canRead() && reader.peek() == '{') {
/* 74 */       extraData = (O)parser.parseAsArgument(reader);
/*    */     } else {
/* 76 */       extraData = (O)ops.emptyMap();
/*    */     } 
/* 78 */     Objects.requireNonNull(ERROR_INVALID_OPTIONS); return (T)(ParticleOptions)type.codec().codec().parse(ops, extraData).getOrThrow(ERROR_INVALID_OPTIONS::create);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 83 */     HolderLookup.RegistryLookup<ParticleType<?>> particles = this.registries.lookupOrThrow(Registries.PARTICLE_TYPE);
/* 84 */     return SharedSuggestionProvider.suggestResource(particles.listElementIds().map(ResourceKey::identifier), builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ParticleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */