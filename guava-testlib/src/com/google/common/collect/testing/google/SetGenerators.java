/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect.testing.google;

import static com.google.common.collect.testing.SampleElements.Strings.AFTER_LAST;
import static com.google.common.collect.testing.SampleElements.Strings.AFTER_LAST_2;
import static com.google.common.collect.testing.SampleElements.Strings.BEFORE_FIRST;
import static com.google.common.collect.testing.SampleElements.Strings.BEFORE_FIRST_2;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.testing.TestCollectionGenerator;
import com.google.common.collect.testing.TestCollidingSetGenerator;
import com.google.common.collect.testing.TestSetGenerator;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.TestStringSortedSetGenerator;
import com.google.common.collect.testing.TestUnhashableCollectionGenerator;
import com.google.common.collect.testing.UnhashableObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Generators of different types of sets and derived collections from sets.
 * 
 * @author Kevin Bourrillion
 * @author Jared Levy
 * @author Hayward Chan
 */
@GwtCompatible
public class SetGenerators {

  public static class ImmutableSetCopyOfGenerator extends TestStringSetGenerator {
    @Override protected Set<String> create(String[] elements) {
      return ImmutableSet.copyOf(elements);
    }
  }

  public static class ImmutableSetWithBadHashesGenerator
      extends TestCollidingSetGenerator
      // Work around a GWT compiler bug.  Not explicitly listing this will
      // cause the createArray() method missing in the generated javascript.
      // TODO: Remove this once the GWT bug is fixed.
      implements TestCollectionGenerator<Object> {
    @Override
    public Set<Object> create(Object... elements) {
      return ImmutableSet.copyOf(elements);
    }
  }

  public static class DegeneratedImmutableSetGenerator
      extends TestStringSetGenerator {
    // Make sure we get what we think we're getting, or else this test
    // is pointless
    @SuppressWarnings("cast")
    @Override protected Set<String> create(String[] elements) {
      return (ImmutableSet<String>)
          ImmutableSet.of(elements[0], elements[0]);
    }
  }

  public static class ImmutableSortedSetCopyOfGenerator
      extends TestStringSortedSetGenerator {
    @Override protected SortedSet<String> create(String[] elements) {
      return ImmutableSortedSet.copyOf(elements);
    }
  }

  public static class ImmutableSortedSetHeadsetGenerator
      extends TestStringSortedSetGenerator {
    @Override protected SortedSet<String> create(String[] elements) {
      List<String> list = Lists.newArrayList(elements);
      list.add("zzz");
      return ImmutableSortedSet.copyOf(list)
          .headSet("zzy");
    }
  }

  public static class ImmutableSortedSetTailsetGenerator
      extends TestStringSortedSetGenerator {
    @Override protected SortedSet<String> create(String[] elements) {
      List<String> list = Lists.newArrayList(elements);
      list.add("\0");
      return ImmutableSortedSet.copyOf(list)
          .tailSet("\0\0");
    }
  }

  public static class ImmutableSortedSetSubsetGenerator
      extends TestStringSortedSetGenerator {
    @Override protected SortedSet<String> create(String[] elements) {
      List<String> list = Lists.newArrayList(elements);
      list.add("\0");
      list.add("zzz");
      return ImmutableSortedSet.copyOf(list)
          .subSet("\0\0", "zzy");
    }
  }

  public static class ImmutableSortedSetExplicitComparator
      extends TestStringSetGenerator {

    private static final Comparator<String> STRING_REVERSED
        = Collections.reverseOrder();

    @Override protected SortedSet<String> create(String[] elements) {
      return ImmutableSortedSet.orderedBy(STRING_REVERSED)
          .add(elements)
          .build();
    }

    @Override public List<String> order(List<String> insertionOrder) {
      Collections.sort(insertionOrder, Collections.reverseOrder());
      return insertionOrder;
    }
  }

  public static class ImmutableSortedSetExplicitSuperclassComparatorGenerator
      extends TestStringSetGenerator {

    private static final Comparator<Comparable<?>> COMPARABLE_REVERSED
        = Collections.reverseOrder();

    @Override protected SortedSet<String> create(String[] elements) {
      return new ImmutableSortedSet.Builder<String>(COMPARABLE_REVERSED)
          .add(elements)
          .build();
    }

    @Override public List<String> order(List<String> insertionOrder) {
      Collections.sort(insertionOrder, Collections.reverseOrder());
      return insertionOrder;
    }
  }

  public static class ImmutableSortedSetReversedOrderGenerator
      extends TestStringSetGenerator {

    @Override protected SortedSet<String> create(String[] elements) {
      return ImmutableSortedSet.<String>reverseOrder()
          .addAll(Arrays.asList(elements).iterator())
          .build();
    }

    @Override public List<String> order(List<String> insertionOrder) {
      Collections.sort(insertionOrder, Collections.reverseOrder());
      return insertionOrder;
    }
  }

  public static class ImmutableSortedSetUnhashableGenerator
      extends TestUnhashableSetGenerator {
    @Override public Set<UnhashableObject> create(
        UnhashableObject[] elements) {
      return ImmutableSortedSet.copyOf(elements);
    }
  }

  public static class ImmutableSetAsListGenerator
      extends TestStringListGenerator {
    @Override protected List<String> create(String[] elements) {
      return ImmutableSet.copyOf(elements).asList();
    }
  }

  public static class ImmutableSortedSetAsListGenerator
      extends TestStringListGenerator {
    @Override protected List<String> create(String[] elements) {
      Comparator<String> comparator = createExplicitComparator(elements);
      ImmutableSet<String> set = ImmutableSortedSet.copyOf(
          comparator, Arrays.asList(elements));
      return set.asList();
    }
  }

  public static class ImmutableSortedSetSubsetAsListGenerator
      extends TestStringListGenerator {
    @Override protected List<String> create(String[] elements) {
      Comparator<String> comparator = createExplicitComparator(elements);
      ImmutableSortedSet.Builder<String> builder
          = ImmutableSortedSet.orderedBy(comparator);
      builder.add(BEFORE_FIRST);
      builder.add(elements);
      builder.add(AFTER_LAST);
      return builder.build().subSet(BEFORE_FIRST_2,
          AFTER_LAST).asList();
    }
  }

  public static class ImmutableSortedSetAsListSubListGenerator
      extends TestStringListGenerator {
    @Override protected List<String> create(String[] elements) {
      Comparator<String> comparator = createExplicitComparator(elements);
      ImmutableSortedSet.Builder<String> builder
          = ImmutableSortedSet.orderedBy(comparator);
      builder.add(BEFORE_FIRST);
      builder.add(elements);
      builder.add(AFTER_LAST);
      return builder.build().asList().subList(1, elements.length + 1);
    }
  }

  public static class ImmutableSortedsetSubsetAsListSubListGenerator
      extends TestStringListGenerator {
    @Override protected List<String> create(String[] elements) {
      Comparator<String> comparator = createExplicitComparator(elements);
      ImmutableSortedSet.Builder<String> builder
          = ImmutableSortedSet.orderedBy(comparator);
      builder.add(BEFORE_FIRST);
      builder.add(BEFORE_FIRST_2);
      builder.add(elements);
      builder.add(AFTER_LAST);
      builder.add(AFTER_LAST_2);
      return builder.build().subSet(BEFORE_FIRST_2,
          AFTER_LAST_2)
              .asList().subList(1, elements.length + 1);
    }
  }

  public abstract static class TestUnhashableSetGenerator
      extends TestUnhashableCollectionGenerator<Set<UnhashableObject>>
      implements TestSetGenerator<UnhashableObject> {
  }

  private static Comparator<String> createExplicitComparator(
      String[] elements) {
    // Collapse equal elements, which Ordering.explicit() doesn't support, while
    // maintaining the ordering by first occurrence.
    Set<String> elementsPlus = Sets.newLinkedHashSet();
    elementsPlus.add(BEFORE_FIRST);
    elementsPlus.add(BEFORE_FIRST_2);
    elementsPlus.addAll(Arrays.asList(elements));
    elementsPlus.add(AFTER_LAST);
    elementsPlus.add(AFTER_LAST_2);
    return Ordering.explicit(Lists.newArrayList(elementsPlus));
  }
}
