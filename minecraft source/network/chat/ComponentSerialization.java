/*     */ package net.minecraft.network.chat;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapDecoder;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.contents.KeybindContents;
/*     */ import net.minecraft.network.chat.contents.NbtContents;
/*     */ import net.minecraft.network.chat.contents.ObjectContents;
/*     */ import net.minecraft.network.chat.contents.PlainTextContents;
/*     */ import net.minecraft.network.chat.contents.ScoreContents;
/*     */ import net.minecraft.network.chat.contents.SelectorContents;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class ComponentSerialization {
/*  38 */   public static final Codec<Component> CODEC = Codec.recursive("Component", ComponentSerialization::createCodec);
/*     */   
/*  40 */   public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
/*  41 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Component>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
/*     */   
/*  43 */   public static final StreamCodec<RegistryFriendlyByteBuf, Component> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
/*  44 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Component>> TRUSTED_OPTIONAL_STREAM_CODEC = TRUSTED_STREAM_CODEC.apply(ByteBufCodecs::optional);
/*     */   
/*  46 */   public static final StreamCodec<ByteBuf, Component> TRUSTED_CONTEXT_FREE_STREAM_CODEC = ByteBufCodecs.fromCodecTrusted(CODEC);
/*     */ 
/*     */   
/*     */   public static Codec<Component> flatRestrictedCodec(final int maxFlatSize) {
/*  50 */     return new Codec<Component>()
/*     */       {
/*     */         public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
/*  53 */           return ComponentSerialization.CODEC.decode(ops, input).flatMap(pair -> {
/*  54 */                 if (isTooLarge(ops, (Component)pair.getFirst())) {
/*  55 */                   return DataResult.error(());
/*     */                 }
/*  57 */                 return DataResult.success(pair);
/*     */               });
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  63 */         public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) { return ComponentSerialization.CODEC.encodeStart(ops, input); }
/*     */ 
/*     */         
/*     */         private <T> boolean isTooLarge(DynamicOps<T> ops, Component input) {
/*  67 */           DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(ComponentSerialization.null.asJsonOps(ops), input);
/*  68 */           return (json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), maxFlatSize));
/*     */         }
/*     */         
/*     */         private static <T> DynamicOps<JsonElement> asJsonOps(DynamicOps<T> ops) {
/*  72 */           if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps)ops;
/*  73 */             return registryOps.withParent(JsonOps.INSTANCE); }
/*     */           
/*  75 */           return JsonOps.INSTANCE;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MutableComponent createFromList(List<Component> list) {
/*  83 */     MutableComponent result = ((Component)list.get(0)).copy();
/*  84 */     for (int i = 1; i < list.size(); i++) {
/*  85 */       result.append((Component)list.get(i));
/*     */     }
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   private static class StrictEither<T>
/*     */     extends MapCodec<T>
/*     */   {
/*     */     private final String typeFieldName;
/*     */     private final MapCodec<T> typed;
/*     */     private final MapCodec<T> fuzzy;
/*     */     
/*     */     public StrictEither(String typeFieldName, MapCodec<T> typed, MapCodec<T> fuzzy) {
/*  98 */       this.typeFieldName = typeFieldName;
/*  99 */       this.typed = typed;
/* 100 */       this.fuzzy = fuzzy;
/*     */     }
/*     */ 
/*     */     
/*     */     public <O> DataResult<T> decode(DynamicOps<O> ops, MapLike<O> input) {
/* 105 */       if (input.get(this.typeFieldName) != null) {
/* 106 */         return this.typed.decode(ops, input);
/*     */       }
/* 108 */       return this.fuzzy.decode(ops, input);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 113 */     public <O> RecordBuilder<O> encode(T input, DynamicOps<O> ops, RecordBuilder<O> prefix) { return this.fuzzy.encode(input, ops, prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     public <T1> Stream<T1> keys(DynamicOps<T1> ops) { return Stream.concat(this.typed.keys(ops), this.fuzzy.keys(ops)).distinct(); }
/*     */   }
/*     */   
/*     */   private static class FuzzyCodec<T>
/*     */     extends MapCodec<T> {
/*     */     private final Collection<MapCodec<? extends T>> codecs;
/*     */     private final Function<T, ? extends MapEncoder<? extends T>> encoderGetter;
/*     */     
/*     */     public FuzzyCodec(Collection<MapCodec<? extends T>> codecs, Function<T, ? extends MapEncoder<? extends T>> encoderGetter) {
/* 127 */       this.codecs = codecs;
/* 128 */       this.encoderGetter = encoderGetter;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <S> DataResult<T> decode(DynamicOps<S> ops, MapLike<S> input) {
/* 134 */       for (MapDecoder<? extends T> codec : this.codecs) {
/* 135 */         DataResult<? extends T> result = codec.decode(ops, input);
/* 136 */         if (result.result().isPresent()) {
/* 137 */           return result;
/*     */         }
/*     */       } 
/*     */       
/* 141 */       return DataResult.error(() -> "No matching codec found");
/*     */     }
/*     */ 
/*     */     
/*     */     public <S> RecordBuilder<S> encode(T input, DynamicOps<S> ops, RecordBuilder<S> prefix) {
/* 146 */       MapEncoder<T> encoder = (MapEncoder)this.encoderGetter.apply(input);
/* 147 */       return encoder.encode(input, ops, prefix);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     public <S> Stream<S> keys(DynamicOps<S> ops) { return this.codecs.stream().flatMap(c -> c.keys(ops)).distinct(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     public String toString() { return "FuzzyCodec[" + String.valueOf(this.codecs) + "]"; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> MapCodec<T> createLegacyComponentMatcher(ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends T>> types, Function<T, MapCodec<? extends T>> codecGetter, String typeFieldName) {
/* 166 */     MapCodec<T> compactCodec = new FuzzyCodec<T>(types.values(), codecGetter);
/*     */ 
/*     */ 
/*     */     
/* 170 */     MapCodec<T> discriminatorCodec = types.codec(Codec.STRING).dispatchMap(typeFieldName, codecGetter, c -> c);
/*     */     
/* 172 */     MapCodec<T> contentsCodec = new StrictEither<T>(typeFieldName, discriminatorCodec, compactCodec);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     return ExtraCodecs.orCompressed(contentsCodec, discriminatorCodec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Codec<Component> createCodec(Codec<Component> topSerializer) {
/* 185 */     ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ComponentContents>> contentTypes = new ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ComponentContents>>();
/*     */     
/* 187 */     bootstrap(contentTypes);
/*     */     
/* 189 */     MapCodec<ComponentContents> compressedContentsCodec = createLegacyComponentMatcher(contentTypes, ComponentContents::codec, "type");
/*     */     
/* 191 */     Codec<Component> fullCodec = RecordCodecBuilder.create(i -> i.group(compressedContentsCodec
/* 192 */           .forGetter(Component::getContents), 
/* 193 */           ExtraCodecs.nonEmptyList(topSerializer.listOf()).optionalFieldOf("extra", List.of())
/* 194 */           .forGetter(Component::getSiblings), Style.Serializer.MAP_CODEC
/* 195 */           .forGetter(Component::getStyle))
/* 196 */         .apply(i, MutableComponent::new));
/*     */ 
/*     */     
/* 199 */     return Codec.either(
/* 200 */         Codec.either(Codec.STRING, 
/*     */           
/* 202 */           ExtraCodecs.nonEmptyList(topSerializer.listOf())), fullCodec)
/*     */ 
/*     */       
/* 205 */       .xmap(specialOrComponent -> 
/* 206 */         (Component)specialOrComponent.map((), ()), component -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 214 */           String text = component.tryCollapseToString();
/* 215 */           return (text != null) ? Either.left(Either.left(text)) : Either.right(component);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void bootstrap(ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ComponentContents>> contentTypes) {
/* 221 */     contentTypes.put("text", PlainTextContents.MAP_CODEC);
/* 222 */     contentTypes.put("translatable", TranslatableContents.MAP_CODEC);
/* 223 */     contentTypes.put("keybind", KeybindContents.MAP_CODEC);
/* 224 */     contentTypes.put("score", ScoreContents.MAP_CODEC);
/* 225 */     contentTypes.put("selector", SelectorContents.MAP_CODEC);
/* 226 */     contentTypes.put("nbt", NbtContents.MAP_CODEC);
/* 227 */     contentTypes.put("object", ObjectContents.MAP_CODEC);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentSerialization.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */