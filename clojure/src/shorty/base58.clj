(ns shorty.base58)

(def ^:private alphabet
  "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ")

(def ^:private base
  (count alphabet))

(defn encode [n]
  (let [char (nth alphabet (rem n base))
        rest (when (>= n base) (encode (quot n base)))]
    (str rest char)))
