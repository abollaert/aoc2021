import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {

    private static final int SIZE_ZERO = 6;
    private static final int SIZE_ONE = 2;
    private static final int SIZE_TWO = 5;
    private static final int SIZE_THREE = 5;
    private static final int SIZE_FOUR = 4;
    private static final int SIZE_FIVE = 5;
    private static final int SIZE_SIX = 6;
    private static final int SIZE_SEVEN = 3;
    private static final int SIZE_EIGHT = 7;
    private static final int SIZE_NINE = 6;

    private static final boolean isOne(final Set<Character> data) {
        return SIZE_ONE == data.size();
    }

    private static final boolean isFour(final Set<Character> data) {
        return SIZE_FOUR == data.size();
    }

    private static final boolean isSeven(final Set<Character> data) {
        return SIZE_SEVEN == data.size();
    }

    private static final boolean isEight(final Set<Character> data) {
        return SIZE_EIGHT == data.size();
    }

    private static final boolean isNine(final Set<Character> data, final Set<Character> four) {
        return data.size() == SIZE_NINE && data.containsAll(four);
    }

    private static final boolean isSix(final Set<Character> data, final Set<Character> one) {
        return data.size() == SIZE_SIX && !data.containsAll(one);
    }

    private static final boolean isZero(final Set<Character> data, final Set<Character> one, final Set<Character> nine) {
        return data.size() == SIZE_ZERO && data.containsAll(one) && !data.equals(nine);
    }

    private static final boolean isFive(final Set<Character> data, final Set<Character> nine, final Set<Character> three) {
        return data.size() == SIZE_FIVE && nine.containsAll(data) && !data.equals(three);
    }

    private static final boolean isThree(final Set<Character> data, final Set<Character> one) {
        return data.size() == SIZE_THREE && data.containsAll(one);
    }

    private static final boolean isTwo(final Set<Character> data, final Set<Character> three, final Set<Character> five) {
        return data.size() == SIZE_TWO && !data.equals(three) && !data.equals(five);
    }


    private record Entry(List<Set<Character>> signals, List<Set<Character>> digits) {

        private final int countUnique() {
            int sum = 0;

            for (final Set<Character> signal : digits) {
                if (isOne(signal) ||
                    isFour(signal) ||
                    isSeven(signal) ||
                    isEight(signal)) {
                    sum += 1;
                }
            }

            return sum;
        }

        private final int output() {
            final Set<Character> one = this.getOne();
            final Set<Character> four = this.getFour();
            final Set<Character> seven = this.getSeven();
            final Set<Character> eight = this.getEight();
            final Set<Character> nine = this.getNine(four);
            final Set<Character> six = this.getSix(one);
            final Set<Character> zero = this.getZero(one, nine);
            final Set<Character> three = this.getThree(one);
            final Set<Character> five = this.getFive(nine, three);
            final Set<Character> two = this.getTwo(three, five);

            /*System.out.println("0 : " + zero);
            System.out.println("1 : " + one);
            System.out.println("2 : " + two);
            System.out.println("3 : " + three);
            System.out.println("4 : " + four);
            System.out.println("5 : " + five);
            System.out.println("6 : " + six);
            System.out.println("7 : " + seven);
            System.out.println("8 : " + eight);
            System.out.println("9 : " + nine);*/
            int output = 0;

            for (int i = 0; i < this.digits.size(); i++) {
                final Set<Character> digit = this.digits.get(i);

                int num = 0;

                if (digit.equals(zero)) {
                    num = 0;
                } else if (digit.equals(one)) {
                    num = 1;
                } else if (digit.equals(two)) {
                    num = 2;
                } else if (digit.equals(three)) {
                    num = 3;
                } else if (digit.equals(four)) {
                    num = 4;
                } else if (digit.equals(five)) {
                    num = 5;
                } else if (digit.equals(six)) {
                    num = 6;
                } else if (digit.equals(seven)) {
                    num = 7;
                } else if (digit.equals(eight)) {
                    num = 8;
                } else if (digit.equals(nine)) {
                    num = 9;
                }

                output += num * Math.pow(10, (3 - i));
            }

            return output;
        }

        private final Set<Character> getFour() {
            for (final Set<Character> signal : this.signals) {
                if (isFour(signal)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getZero(final Set<Character> one, final Set<Character> nine) {
            for (final Set<Character> signal : this.signals) {
                if (isZero(signal, one, nine)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getOne() {
            for (final Set<Character> signal : this.signals) {
                if (isOne(signal)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getSeven() {
            for (final Set<Character> signal : this.signals) {
                if (isSeven(signal)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getEight() {
            for (final Set<Character> signal : this.signals) {
                if (isEight(signal)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getNine(final Set<Character> four) {
            for (final Set<Character> signal : this.signals) {
                if (isNine(signal, four)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getSix(final Set<Character> one) {
            for (final Set<Character> signal : this.signals) {
                if (isSix(signal, one)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getFive(final Set<Character> nine, final Set<Character> three) {
            for (final Set<Character> signal : this.signals) {
                if (isFive(signal, nine, three)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getThree(final Set<Character> one) {
            for (final Set<Character> signal : this.signals) {
                if (isThree(signal, one)) {
                    return signal;
                }
            }

            return null;
        }

        private final Set<Character> getTwo(final Set<Character> three, final Set<Character> five) {
            for (final Set<Character> signal : this.signals) {
                if (isTwo(signal, three, five)) {
                    return signal;
                }
            }

            return null;
        }

    }

    private static List<Entry> readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day8.class.getResourceAsStream("/day8.txt")))) {
            final List<Entry> data = new ArrayList<>();

            String line = reader.readLine();

            while (line != null) {
                final String[] patternsAndDigits = line.split("\\|");

                final String[] patternStrings = patternsAndDigits[0].trim().split("\s+");
                final String[] digitStrings = patternsAndDigits[1].trim().split("\s+");

                final List<Set<Character>> patterns = new ArrayList<>();

                for (final String pattern : patternStrings) {
                    patterns.add(pattern.chars()
                                        .mapToObj(i -> Character.valueOf((char) i))
                                        .collect(Collectors.toSet()));
                }

                final List<Set<Character>> digits = new ArrayList<>();

                for (final String digit : digitStrings) {
                    digits.add(digit.chars()
                                    .mapToObj(i -> Character.valueOf((char) i))
                                    .collect(Collectors.toSet()));
                }

                data.add(new Entry(patterns, digits));

                line = reader.readLine();
            }

            return data;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int countUnique(final List<Entry> data) {
        int sum = 0;

        for (final Entry entry : data) {
            sum += entry.countUnique();
        }

        return sum;
    }

    public static void main(String[] args) {
        final List<Entry> data = readData();

        System.out.println(countUnique(data));

        int sum = 0;

        for (final Entry entry : data) {
            sum += entry.output();
        }

        System.out.println(sum);
    }
}
