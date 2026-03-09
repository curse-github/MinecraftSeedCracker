/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class SuggestionProviders
/*    */ {
/* 18 */   private static final Map<Identifier, SuggestionProvider<SharedSuggestionProvider>> PROVIDERS_BY_NAME = new HashMap();
/* 19 */   private static final Identifier ID_ASK_SERVER = Identifier.withDefaultNamespace("ask_server");
/*    */   
/* 21 */   public static final SuggestionProvider<SharedSuggestionProvider> ASK_SERVER = register(ID_ASK_SERVER, (c, p) -> ((SharedSuggestionProvider)c.getSource()).customSuggestion(c));
/* 22 */   public static final SuggestionProvider<SharedSuggestionProvider> AVAILABLE_SOUNDS = register(Identifier.withDefaultNamespace("available_sounds"), (c, p) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)c.getSource()).getAvailableSounds(), p));
/* 23 */   public static final SuggestionProvider<SharedSuggestionProvider> SUMMONABLE_ENTITIES = register(Identifier.withDefaultNamespace("summonable_entities"), (c, p) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ENTITY_TYPE.stream().filter(()), p, EntityType::getKey, EntityType::getDescription));
/*    */ 
/*    */   
/*    */   public static <S extends SharedSuggestionProvider> SuggestionProvider<S> register(Identifier name, SuggestionProvider<SharedSuggestionProvider> provider) {
/* 27 */     SuggestionProvider<SharedSuggestionProvider> previous = (SuggestionProvider)PROVIDERS_BY_NAME.putIfAbsent(name, provider);
/* 28 */     if (previous != null) {
/* 29 */       throw new IllegalArgumentException("A command suggestion provider is already registered with the name '" + String.valueOf(name) + "'");
/*    */     }
/* 31 */     return new RegisteredSuggestion(name, provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static <S extends SharedSuggestionProvider> SuggestionProvider<S> cast(SuggestionProvider<SharedSuggestionProvider> provider) { return provider; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static <S extends SharedSuggestionProvider> SuggestionProvider<S> getProvider(Identifier name) { return cast((SuggestionProvider)PROVIDERS_BY_NAME.getOrDefault(name, ASK_SERVER)); }
/*    */ 
/*    */   
/*    */   public static Identifier getName(SuggestionProvider<?> provider) {
/* 46 */     RegisteredSuggestion registeredProvider = (RegisteredSuggestion)provider; return (provider instanceof RegisteredSuggestion) ? registeredProvider.name : ID_ASK_SERVER;
/*    */   }
/*    */   private static final class RegisteredSuggestion extends Record implements SuggestionProvider<SharedSuggestionProvider> { private final Identifier name; private final SuggestionProvider<SharedSuggestionProvider> delegate;
/* 49 */     private RegisteredSuggestion(Identifier name, SuggestionProvider<SharedSuggestionProvider> delegate) { this.name = name; this.delegate = delegate; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 49 */       //   0	7	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion; } public Identifier name() { return this.name; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;
/* 49 */       //   0	8	1	o	Ljava/lang/Object; } public SuggestionProvider<SharedSuggestionProvider> delegate() { return this.delegate; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     public CompletableFuture<Suggestions> getSuggestions(CommandContext<SharedSuggestionProvider> context, SuggestionsBuilder builder) throws CommandSyntaxException { return this.delegate.getSuggestions(context, builder); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\SuggestionProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */