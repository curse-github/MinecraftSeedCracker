/*     */ package net.minecraft.network.chat.contents;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.data.DataSources;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NbtContents implements ComponentContents {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  34 */   public static final MapCodec<NbtContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/*  35 */         .fieldOf("nbt").forGetter(NbtContents::getNbtPath), Codec.BOOL
/*  36 */         .lenientOptionalFieldOf("interpret", Boolean.valueOf(false)).forGetter(NbtContents::isInterpreting), ComponentSerialization.CODEC
/*  37 */         .lenientOptionalFieldOf("separator").forGetter(NbtContents::getSeparator), DataSources.CODEC
/*  38 */         .forGetter(NbtContents::getDataSource))
/*  39 */       .apply(i, NbtContents::new));
/*     */   
/*     */   private final boolean interpreting;
/*     */   
/*     */   private final Optional<Component> separator;
/*     */   
/*     */   private final String nbtPathPattern;
/*     */   private final DataSource dataSource;
/*     */   protected final NbtPathArgument.NbtPath compiledNbtPath;
/*     */   
/*  49 */   public NbtContents(String nbtPath, boolean interpreting, Optional<Component> separator, DataSource dataSource) { this(nbtPath, compileNbtPath(nbtPath), interpreting, separator, dataSource); }
/*     */ 
/*     */   
/*     */   private NbtContents(String nbtPathPattern, NbtPathArgument.NbtPath compiledNbtPath, boolean interpreting, Optional<Component> separator, DataSource dataSource) {
/*  53 */     this.nbtPathPattern = nbtPathPattern;
/*  54 */     this.compiledNbtPath = compiledNbtPath;
/*  55 */     this.interpreting = interpreting;
/*  56 */     this.separator = separator;
/*  57 */     this.dataSource = dataSource;
/*     */   }
/*     */   
/*     */   private static NbtPathArgument.NbtPath compileNbtPath(String path) {
/*     */     try {
/*  62 */       return (new NbtPathArgument()).parse(new StringReader(path));
/*  63 */     } catch (CommandSyntaxException ex) {
/*  64 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  69 */   public String getNbtPath() { return this.nbtPathPattern; }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public boolean isInterpreting() { return this.interpreting; }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public Optional<Component> getSeparator() { return this.separator; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public DataSource getDataSource() { return this.dataSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/contents/NbtContents
/*     */     //   11: ifeq -> 78
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/contents/NbtContents
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield dataSource : Lnet/minecraft/network/chat/contents/data/DataSource;
/*     */     //   23: aload_2
/*     */     //   24: getfield dataSource : Lnet/minecraft/network/chat/contents/data/DataSource;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 78
/*     */     //   35: aload_0
/*     */     //   36: getfield separator : Ljava/util/Optional;
/*     */     //   39: aload_2
/*     */     //   40: getfield separator : Ljava/util/Optional;
/*     */     //   43: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 78
/*     */     //   49: aload_0
/*     */     //   50: getfield interpreting : Z
/*     */     //   53: aload_2
/*     */     //   54: getfield interpreting : Z
/*     */     //   57: if_icmpne -> 78
/*     */     //   60: aload_0
/*     */     //   61: getfield nbtPathPattern : Ljava/lang/String;
/*     */     //   64: aload_2
/*     */     //   65: getfield nbtPathPattern : Ljava/lang/String;
/*     */     //   68: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   71: ifeq -> 78
/*     */     //   74: iconst_1
/*     */     //   75: goto -> 79
/*     */     //   78: iconst_0
/*     */     //   79: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     //   #87	-> 5
/*     */     //   #93	-> 7
/*     */     //   #89	-> 14
/*     */     //   #90	-> 27
/*     */     //   #91	-> 43
/*     */     //   #93	-> 68
/*     */     //   #89	-> 79
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	59	2	that	Lnet/minecraft/network/chat/contents/NbtContents;
/*     */     //   0	80	0	this	Lnet/minecraft/network/chat/contents/NbtContents;
/*     */     //   0	80	1	o	Ljava/lang/Object; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     result = this.interpreting ? 1 : 0;
/*  99 */     result = 31 * result + this.separator.hashCode();
/* 100 */     result = 31 * result + this.nbtPathPattern.hashCode();
/* 101 */     return 31 * result + this.dataSource.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public String toString() { return "nbt{" + String.valueOf(this.dataSource) + ", interpreting=" + this.interpreting + ", separator=" + String.valueOf(this.separator) + "}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 115 */     if (source == null || this.compiledNbtPath == null) {
/* 116 */       return Component.empty();
/*     */     }
/*     */     
/* 119 */     Stream<Tag> elements = this.dataSource.getData(source).flatMap(t -> {
/*     */           try {
/* 121 */             return this.compiledNbtPath.get(t).stream();
/* 122 */           } catch (CommandSyntaxException ignored) {
/* 123 */             return Stream.empty();
/*     */           } 
/*     */         });
/*     */     
/* 127 */     if (this.interpreting) {
/* 128 */       RegistryOps<Tag> registryOps = source.registryAccess().createSerializationContext(NbtOps.INSTANCE);
/* 129 */       Component resolvedSeparator = (Component)DataFixUtils.orElse(ComponentUtils.updateForEntity(source, this.separator, entity, recursionDepth), ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR);
/* 130 */       return (MutableComponent)elements.flatMap(tag -> {
/*     */             try {
/* 132 */               Component component = (Component)ComponentSerialization.CODEC.parse(registryOps, tag).getOrThrow();
/* 133 */               return Stream.of(ComponentUtils.updateForEntity(source, component, entity, recursionDepth));
/* 134 */             } catch (Exception e) {
/* 135 */               LOGGER.warn("Failed to parse component: {}", tag, e);
/* 136 */               return Stream.of(new MutableComponent[0]);
/*     */             }
/*     */           
/* 139 */           }).reduce((left, right) -> left.append(resolvedSeparator).append(right))
/* 140 */         .orElseGet(Component::empty);
/*     */     } 
/* 142 */     Stream<String> stringElements = elements.map(NbtContents::asString);
/* 143 */     return (MutableComponent)ComponentUtils.updateForEntity(source, this.separator, entity, recursionDepth)
/* 144 */       .map(resolvedSeparator -> 
/* 145 */         (MutableComponent)stringElements.map(Component::literal).reduce(()).orElseGet(Component::empty))
/* 146 */       .orElseGet(() -> 
/* 147 */         Component.literal((String)stringElements.collect(Collectors.joining(", "))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String asString(Tag tag) {
/* 154 */     if (tag instanceof StringTag) { stringTag = (StringTag)tag; try { String str; return str = stringTag.value(); } catch (Throwable stringTag) { throw new MatchException(stringTag.toString(), stringTag); }
/*     */        }
/*     */     
/* 157 */     return tag.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public MapCodec<NbtContents> codec() { return MAP_CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\NbtContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */