# Oh Yeah! Layout Style Guide:

## Contents:

* [Ordering](#ordering)
  * [General Ordering](#general-ordering)
  * [Ordering Within Subject](#ordering-within-subject)
  * [Backwards Compatibility](#backwards-compatibility)
* [Spacing](#spacing)
* [Tag Endings](#tag-endings)

## Ordering

The philosophy of this ordering is to list the element's global
identifiers at the top, followed by its content and behavior.

### General Ordering

    1. id
    2. width, height
    3. orientation
    4. alignment, constraints, etc.
    5. layout gravity, gravity
    6. margin, padding
    7. text
    8. image
    9. content description
    10. visibility
    11. onClick
    12. "app:" tags
    13. style tag
    14. "tools:" tags

### Ordering Within Subject

Within this general ordering, multiple elements within
the same subject should be ordered alphabetically. For instance:

```
android:layout_marginBottom="..."
android:layout_marginEnd="..."
android:layout_marginStart="..."
android:layout_marginTop="..."
```

### Backwards Compatibility

Alternate attributes for backwards compatibility should be listed
directly under the original attribute. For instance:

```
android:layout_toEndOf="..."
android:layout_toRightOf="..."
android:layout_toStartOf="..."
android:layout_toLeftOf="..."
```

## Spacing

There should be empty lines between elements. Closing tags should
have no spaces between them.

## Tag Endings

If a tag ends with a  `/>`, it should have a space before it,
like `android:textStyle="bold" />`.
