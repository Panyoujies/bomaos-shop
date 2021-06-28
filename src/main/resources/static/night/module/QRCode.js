layui.define(function (v) {
    var l;

    function d(A) {
        this.mode = c.MODE_8BIT_BYTE;
        this.data = A;
        this.parsedData = [];
        for (var y = 0, x = this.data.length; y < x; y++) {
            var w = [];
            var z = this.data.charCodeAt(y);
            if (z > 65536) {
                w[0] = 240 | ((z & 1835008) >>> 18);
                w[1] = 128 | ((z & 258048) >>> 12);
                w[2] = 128 | ((z & 4032) >>> 6);
                w[3] = 128 | (z & 63)
            } else {
                if (z > 2048) {
                    w[0] = 224 | ((z & 61440) >>> 12);
                    w[1] = 128 | ((z & 4032) >>> 6);
                    w[2] = 128 | (z & 63)
                } else {
                    if (z > 128) {
                        w[0] = 192 | ((z & 1984) >>> 6);
                        w[1] = 128 | (z & 63)
                    } else {
                        w[0] = z
                    }
                }
            }
            this.parsedData.push(w)
        }
        this.parsedData = Array.prototype.concat.apply([], this.parsedData);
        if (this.parsedData.length != this.data.length) {
            this.parsedData.unshift(191);
            this.parsedData.unshift(187);
            this.parsedData.unshift(239)
        }
    }

    d.prototype = {
        getLength: function (i) {
            return this.parsedData.length
        }, write: function (x) {
            for (var y = 0, w = this.parsedData.length; y < w; y++) {
                x.put(this.parsedData[y], 8)
            }
        }
    };

    function s(w, i) {
        this.typeNumber = w;
        this.errorCorrectLevel = i;
        this.modules = null;
        this.moduleCount = 0;
        this.dataCache = null;
        this.dataList = []
    }

    s.prototype = {
        addData: function (w) {
            var i = new d(w);
            this.dataList.push(i);
            this.dataCache = null
        }, isDark: function (w, i) {
            if (w < 0 || this.moduleCount <= w || i < 0 || this.moduleCount <= i) {
                throw new Error(w + "," + i)
            }
            return this.modules[w][i]
        }, getModuleCount: function () {
            return this.moduleCount
        }, make: function () {
            this.makeImpl(false, this.getBestMaskPattern())
        }, makeImpl: function (y, x) {
            this.moduleCount = this.typeNumber * 4 + 17;
            this.modules = new Array(this.moduleCount);
            for (var w = 0; w < this.moduleCount; w++) {
                this.modules[w] = new Array(this.moduleCount);
                for (var i = 0; i < this.moduleCount; i++) {
                    this.modules[w][i] = null
                }
            }
            this.setupPositionProbePattern(0, 0);
            this.setupPositionProbePattern(this.moduleCount - 7, 0);
            this.setupPositionProbePattern(0, this.moduleCount - 7);
            this.setupPositionAdjustPattern();
            this.setupTimingPattern();
            this.setupTypeInfo(y, x);
            if (this.typeNumber >= 7) {
                this.setupTypeNumber(y)
            }
            if (this.dataCache == null) {
                this.dataCache = s.createData(this.typeNumber, this.errorCorrectLevel, this.dataList)
            }
            this.mapData(this.dataCache, x)
        }, setupPositionProbePattern: function (x, i) {
            for (var w = -1; w <= 7; w++) {
                if (x + w <= -1 || this.moduleCount <= x + w) {
                    continue
                }
                for (var y = -1; y <= 7; y++) {
                    if (i + y <= -1 || this.moduleCount <= i + y) {
                        continue
                    }
                    if ((0 <= w && w <= 6 && (y == 0 || y == 6)) || (0 <= y && y <= 6 && (w == 0 || w == 6)) || (2 <= w && w <= 4 && 2 <= y && y <= 4)) {
                        this.modules[x + w][i + y] = true
                    } else {
                        this.modules[x + w][i + y] = false
                    }
                }
            }
        }, getBestMaskPattern: function () {
            var z = 0;
            var y = 0;
            for (var x = 0; x < 8; x++) {
                this.makeImpl(true, x);
                var w = k.getLostPoint(this);
                if (x == 0 || z > w) {
                    z = w;
                    y = x
                }
            }
            return y
        }, createMovieClip: function (B, i, z) {
            var F = B.createEmptyMovieClip(i, z);
            var A = 1;
            this.make();
            for (var G = 0; G < this.modules.length; G++) {
                var D = G * A;
                for (var w = 0; w < this.modules[G].length; w++) {
                    var E = w * A;
                    var C = this.modules[G][w];
                    if (C) {
                        F.beginFill(0, 100);
                        F.moveTo(E, D);
                        F.lineTo(E + A, D);
                        F.lineTo(E + A, D + A);
                        F.lineTo(E, D + A);
                        F.endFill()
                    }
                }
            }
            return F
        }, setupTimingPattern: function () {
            for (var i = 8; i < this.moduleCount - 8; i++) {
                if (this.modules[i][6] != null) {
                    continue
                }
                this.modules[i][6] = (i % 2 == 0)
            }
            for (var w = 8; w < this.moduleCount - 8; w++) {
                if (this.modules[6][w] != null) {
                    continue
                }
                this.modules[6][w] = (w % 2 == 0)
            }
        }, setupPositionAdjustPattern: function () {
            var C = k.getPatternPosition(this.typeNumber);
            for (var y = 0; y < C.length; y++) {
                for (var x = 0; x < C.length; x++) {
                    var A = C[y];
                    var w = C[x];
                    if (this.modules[A][w] != null) {
                        continue
                    }
                    for (var z = -2; z <= 2; z++) {
                        for (var B = -2; B <= 2; B++) {
                            if (z == -2 || z == 2 || B == -2 || B == 2 || (z == 0 && B == 0)) {
                                this.modules[A + z][w + B] = true
                            } else {
                                this.modules[A + z][w + B] = false
                            }
                        }
                    }
                }
            }
        }, setupTypeNumber: function (z) {
            var y = k.getBCHTypeNumber(this.typeNumber);
            for (var x = 0; x < 18; x++) {
                var w = (!z && ((y >> x) & 1) == 1);
                this.modules[Math.floor(x / 3)][x % 3 + this.moduleCount - 8 - 3] = w
            }
            for (var x = 0; x < 18; x++) {
                var w = (!z && ((y >> x) & 1) == 1);
                this.modules[x % 3 + this.moduleCount - 8 - 3][Math.floor(x / 3)] = w
            }
        }, setupTypeInfo: function (B, A) {
            var z = (this.errorCorrectLevel << 3) | A;
            var y = k.getBCHTypeInfo(z);
            for (var x = 0; x < 15; x++) {
                var w = (!B && ((y >> x) & 1) == 1);
                if (x < 6) {
                    this.modules[x][8] = w
                } else {
                    if (x < 8) {
                        this.modules[x + 1][8] = w
                    } else {
                        this.modules[this.moduleCount - 15 + x][8] = w
                    }
                }
            }
            for (var x = 0; x < 15; x++) {
                var w = (!B && ((y >> x) & 1) == 1);
                if (x < 8) {
                    this.modules[8][this.moduleCount - x - 1] = w
                } else {
                    if (x < 9) {
                        this.modules[8][15 - x - 1 + 1] = w
                    } else {
                        this.modules[8][15 - x - 1] = w
                    }
                }
            }
            this.modules[this.moduleCount - 8][8] = (!B)
        }, mapData: function (A, w) {
            var y = -1;
            var E = this.moduleCount - 1;
            var z = 7;
            var i = 0;
            for (var x = this.moduleCount - 1; x > 0; x -= 2) {
                if (x == 6) {
                    x--
                }
                while (true) {
                    for (var C = 0; C < 2; C++) {
                        if (this.modules[E][x - C] == null) {
                            var B = false;
                            if (i < A.length) {
                                B = (((A[i] >>> z) & 1) == 1)
                            }
                            var D = k.getMask(w, E, x - C);
                            if (D) {
                                B = !B
                            }
                            this.modules[E][x - C] = B;
                            z--;
                            if (z == -1) {
                                i++;
                                z = 7
                            }
                        }
                    }
                    E += y;
                    if (E < 0 || this.moduleCount <= E) {
                        E -= y;
                        y = -y;
                        break
                    }
                }
            }
        }
    };
    s.PAD0 = 236;
    s.PAD1 = 17;
    s.createData = function (D, C, z) {
        var x = r.getRSBlocks(D, C);
        var w = new m();
        for (var y = 0; y < z.length; y++) {
            var B = z[y];
            w.put(B.mode, 4);
            w.put(B.getLength(), k.getLengthInBits(B.mode, D));
            B.write(w)
        }
        var A = 0;
        for (var y = 0; y < x.length; y++) {
            A += x[y].dataCount
        }
        if (w.getLengthInBits() > A * 8) {
            throw new Error("code length overflow. (" + w.getLengthInBits() + ">" + A * 8 + ")")
        }
        if (w.getLengthInBits() + 4 <= A * 8) {
            w.put(0, 4)
        }
        while (w.getLengthInBits() % 8 != 0) {
            w.putBit(false)
        }
        while (true) {
            if (w.getLengthInBits() >= A * 8) {
                break
            }
            w.put(s.PAD0, 8);
            if (w.getLengthInBits() >= A * 8) {
                break
            }
            w.put(s.PAD1, 8)
        }
        return s.createBytes(w, x)
    };
    s.createBytes = function (G, J) {
        var y = 0;
        var M = 0;
        var K = 0;
        var x = new Array(J.length);
        var B = new Array(J.length);
        for (var E = 0; E < J.length; E++) {
            var F = J[E].dataCount;
            var w = J[E].totalCount - F;
            M = Math.max(M, F);
            K = Math.max(K, w);
            x[E] = new Array(F);
            for (var H = 0; H < x[E].length; H++) {
                x[E][H] = 255 & G.buffer[H + y]
            }
            y += F;
            var C = k.getErrorCorrectPolynomial(w);
            var L = new q(x[E], C.getLength() - 1);
            var z = L.mod(C);
            B[E] = new Array(C.getLength() - 1);
            for (var H = 0; H < B[E].length; H++) {
                var D = H + z.getLength() - B[E].length;
                B[E][H] = (D >= 0) ? z.get(D) : 0
            }
        }
        var I = 0;
        for (var H = 0; H < J.length; H++) {
            I += J[H].totalCount
        }
        var N = new Array(I);
        var A = 0;
        for (var H = 0; H < M; H++) {
            for (var E = 0; E < J.length; E++) {
                if (H < x[E].length) {
                    N[A++] = x[E][H]
                }
            }
        }
        for (var H = 0; H < K; H++) {
            for (var E = 0; E < J.length; E++) {
                if (H < B[E].length) {
                    N[A++] = B[E][H]
                }
            }
        }
        return N
    };
    var c = {MODE_NUMBER: 1 << 0, MODE_ALPHA_NUM: 1 << 1, MODE_8BIT_BYTE: 1 << 2, MODE_KANJI: 1 << 3};
    var f = {L: 1, M: 0, Q: 3, H: 2};
    var u = {
        PATTERN000: 0,
        PATTERN001: 1,
        PATTERN010: 2,
        PATTERN011: 3,
        PATTERN100: 4,
        PATTERN101: 5,
        PATTERN110: 6,
        PATTERN111: 7
    };
    var k = {
        PATTERN_POSITION_TABLE: [[], [6, 18], [6, 22], [6, 26], [6, 30], [6, 34], [6, 22, 38], [6, 24, 42], [6, 26, 46], [6, 28, 50], [6, 30, 54], [6, 32, 58], [6, 34, 62], [6, 26, 46, 66], [6, 26, 48, 70], [6, 26, 50, 74], [6, 30, 54, 78], [6, 30, 56, 82], [6, 30, 58, 86], [6, 34, 62, 90], [6, 28, 50, 72, 94], [6, 26, 50, 74, 98], [6, 30, 54, 78, 102], [6, 28, 54, 80, 106], [6, 32, 58, 84, 110], [6, 30, 58, 86, 114], [6, 34, 62, 90, 118], [6, 26, 50, 74, 98, 122], [6, 30, 54, 78, 102, 126], [6, 26, 52, 78, 104, 130], [6, 30, 56, 82, 108, 134], [6, 34, 60, 86, 112, 138], [6, 30, 58, 86, 114, 142], [6, 34, 62, 90, 118, 146], [6, 30, 54, 78, 102, 126, 150], [6, 24, 50, 76, 102, 128, 154], [6, 28, 54, 80, 106, 132, 158], [6, 32, 58, 84, 110, 136, 162], [6, 26, 54, 82, 110, 138, 166], [6, 30, 58, 86, 114, 142, 170]],
        G15: (1 << 10) | (1 << 8) | (1 << 5) | (1 << 4) | (1 << 2) | (1 << 1) | (1 << 0),
        G18: (1 << 12) | (1 << 11) | (1 << 10) | (1 << 9) | (1 << 8) | (1 << 5) | (1 << 2) | (1 << 0),
        G15_MASK: (1 << 14) | (1 << 12) | (1 << 10) | (1 << 4) | (1 << 1),
        getBCHTypeInfo: function (i) {
            var w = i << 10;
            while (k.getBCHDigit(w) - k.getBCHDigit(k.G15) >= 0) {
                w ^= (k.G15 << (k.getBCHDigit(w) - k.getBCHDigit(k.G15)))
            }
            return ((i << 10) | w) ^ k.G15_MASK
        },
        getBCHTypeNumber: function (i) {
            var w = i << 12;
            while (k.getBCHDigit(w) - k.getBCHDigit(k.G18) >= 0) {
                w ^= (k.G18 << (k.getBCHDigit(w) - k.getBCHDigit(k.G18)))
            }
            return (i << 12) | w
        },
        getBCHDigit: function (i) {
            var w = 0;
            while (i != 0) {
                w++;
                i >>>= 1
            }
            return w
        },
        getPatternPosition: function (i) {
            return k.PATTERN_POSITION_TABLE[i - 1]
        },
        getMask: function (y, x, w) {
            switch (y) {
                case u.PATTERN000:
                    return (x + w) % 2 == 0;
                case u.PATTERN001:
                    return x % 2 == 0;
                case u.PATTERN010:
                    return w % 3 == 0;
                case u.PATTERN011:
                    return (x + w) % 3 == 0;
                case u.PATTERN100:
                    return (Math.floor(x / 2) + Math.floor(w / 3)) % 2 == 0;
                case u.PATTERN101:
                    return (x * w) % 2 + (x * w) % 3 == 0;
                case u.PATTERN110:
                    return ((x * w) % 2 + (x * w) % 3) % 2 == 0;
                case u.PATTERN111:
                    return ((x * w) % 3 + (x + w) % 2) % 2 == 0;
                default:
                    throw new Error("bad maskPattern:" + y)
            }
        },
        getErrorCorrectPolynomial: function (x) {
            var w = new q([1], 0);
            for (var y = 0; y < x; y++) {
                w = w.multiply(new q([1, p.gexp(y)], 0))
            }
            return w
        },
        getLengthInBits: function (w, i) {
            if (1 <= i && i < 10) {
                switch (w) {
                    case c.MODE_NUMBER:
                        return 10;
                    case c.MODE_ALPHA_NUM:
                        return 9;
                    case c.MODE_8BIT_BYTE:
                        return 8;
                    case c.MODE_KANJI:
                        return 8;
                    default:
                        throw new Error("mode:" + w)
                }
            } else {
                if (i < 27) {
                    switch (w) {
                        case c.MODE_NUMBER:
                            return 12;
                        case c.MODE_ALPHA_NUM:
                            return 11;
                        case c.MODE_8BIT_BYTE:
                            return 16;
                        case c.MODE_KANJI:
                            return 10;
                        default:
                            throw new Error("mode:" + w)
                    }
                } else {
                    if (i < 41) {
                        switch (w) {
                            case c.MODE_NUMBER:
                                return 14;
                            case c.MODE_ALPHA_NUM:
                                return 13;
                            case c.MODE_8BIT_BYTE:
                                return 16;
                            case c.MODE_KANJI:
                                return 12;
                            default:
                                throw new Error("mode:" + w)
                        }
                    } else {
                        throw new Error("type:" + i)
                    }
                }
            }
        },
        getLostPoint: function (w) {
            var y = w.getModuleCount();
            var z = 0;
            for (var G = 0; G < y; G++) {
                for (var x = 0; x < y; x++) {
                    var E = 0;
                    var D = w.isDark(G, x);
                    for (var i = -1; i <= 1; i++) {
                        if (G + i < 0 || y <= G + i) {
                            continue
                        }
                        for (var C = -1; C <= 1; C++) {
                            if (x + C < 0 || y <= x + C) {
                                continue
                            }
                            if (i == 0 && C == 0) {
                                continue
                            }
                            if (D == w.isDark(G + i, x + C)) {
                                E++
                            }
                        }
                    }
                    if (E > 5) {
                        z += (3 + E - 5)
                    }
                }
            }
            for (var G = 0; G < y - 1; G++) {
                for (var x = 0; x < y - 1; x++) {
                    var A = 0;
                    if (w.isDark(G, x)) {
                        A++
                    }
                    if (w.isDark(G + 1, x)) {
                        A++
                    }
                    if (w.isDark(G, x + 1)) {
                        A++
                    }
                    if (w.isDark(G + 1, x + 1)) {
                        A++
                    }
                    if (A == 0 || A == 4) {
                        z += 3
                    }
                }
            }
            for (var G = 0; G < y; G++) {
                for (var x = 0; x < y - 6; x++) {
                    if (w.isDark(G, x) && !w.isDark(G, x + 1) && w.isDark(G, x + 2) && w.isDark(G, x + 3) && w.isDark(G, x + 4) && !w.isDark(G, x + 5) && w.isDark(G, x + 6)) {
                        z += 40
                    }
                }
            }
            for (var x = 0; x < y; x++) {
                for (var G = 0; G < y - 6; G++) {
                    if (w.isDark(G, x) && !w.isDark(G + 1, x) && w.isDark(G + 2, x) && w.isDark(G + 3, x) && w.isDark(G + 4, x) && !w.isDark(G + 5, x) && w.isDark(G + 6, x)) {
                        z += 40
                    }
                }
            }
            var F = 0;
            for (var x = 0; x < y; x++) {
                for (var G = 0; G < y; G++) {
                    if (w.isDark(G, x)) {
                        F++
                    }
                }
            }
            var B = Math.abs(100 * F / y / y - 50) / 5;
            z += B * 10;
            return z
        }
    };
    var p = {
        glog: function (i) {
            if (i < 1) {
                throw new Error("glog(" + i + ")")
            }
            return p.LOG_TABLE[i]
        }, gexp: function (i) {
            while (i < 0) {
                i += 255
            }
            while (i >= 256) {
                i -= 255
            }
            return p.EXP_TABLE[i]
        }, EXP_TABLE: new Array(256), LOG_TABLE: new Array(256)
    };
    for (var o = 0; o < 8; o++) {
        p.EXP_TABLE[o] = 1 << o
    }
    for (var o = 8; o < 256; o++) {
        p.EXP_TABLE[o] = p.EXP_TABLE[o - 4] ^ p.EXP_TABLE[o - 5] ^ p.EXP_TABLE[o - 6] ^ p.EXP_TABLE[o - 8]
    }
    for (var o = 0; o < 255; o++) {
        p.LOG_TABLE[p.EXP_TABLE[o]] = o
    }

    function q(x, w) {
        if (x.length == undefined) {
            throw new Error(x.length + "/" + w)
        }
        var z = 0;
        while (z < x.length && x[z] == 0) {
            z++
        }
        this.num = new Array(x.length - z + w);
        for (var y = 0; y < x.length - z; y++) {
            this.num[y] = x[y + z]
        }
    }

    q.prototype = {
        get: function (i) {
            return this.num[i]
        }, getLength: function () {
            return this.num.length
        }, multiply: function (z) {
            var x = new Array(this.getLength() + z.getLength() - 1);
            for (var y = 0; y < this.getLength(); y++) {
                for (var w = 0; w < z.getLength(); w++) {
                    x[y + w] ^= p.gexp(p.glog(this.get(y)) + p.glog(z.get(w)))
                }
            }
            return new q(x, 0)
        }, mod: function (z) {
            if (this.getLength() - z.getLength() < 0) {
                return this
            }
            var y = p.glog(this.get(0)) - p.glog(z.get(0));
            var w = new Array(this.getLength());
            for (var x = 0; x < this.getLength(); x++) {
                w[x] = this.get(x)
            }
            for (var x = 0; x < z.getLength(); x++) {
                w[x] ^= p.gexp(p.glog(z.get(x)) + y)
            }
            return new q(w, 0).mod(z)
        }
    };

    function r(i, w) {
        this.totalCount = i;
        this.dataCount = w
    }

    r.RS_BLOCK_TABLE = [[1, 26, 19], [1, 26, 16], [1, 26, 13], [1, 26, 9], [1, 44, 34], [1, 44, 28], [1, 44, 22], [1, 44, 16], [1, 70, 55], [1, 70, 44], [2, 35, 17], [2, 35, 13], [1, 100, 80], [2, 50, 32], [2, 50, 24], [4, 25, 9], [1, 134, 108], [2, 67, 43], [2, 33, 15, 2, 34, 16], [2, 33, 11, 2, 34, 12], [2, 86, 68], [4, 43, 27], [4, 43, 19], [4, 43, 15], [2, 98, 78], [4, 49, 31], [2, 32, 14, 4, 33, 15], [4, 39, 13, 1, 40, 14], [2, 121, 97], [2, 60, 38, 2, 61, 39], [4, 40, 18, 2, 41, 19], [4, 40, 14, 2, 41, 15], [2, 146, 116], [3, 58, 36, 2, 59, 37], [4, 36, 16, 4, 37, 17], [4, 36, 12, 4, 37, 13], [2, 86, 68, 2, 87, 69], [4, 69, 43, 1, 70, 44], [6, 43, 19, 2, 44, 20], [6, 43, 15, 2, 44, 16], [4, 101, 81], [1, 80, 50, 4, 81, 51], [4, 50, 22, 4, 51, 23], [3, 36, 12, 8, 37, 13], [2, 116, 92, 2, 117, 93], [6, 58, 36, 2, 59, 37], [4, 46, 20, 6, 47, 21], [7, 42, 14, 4, 43, 15], [4, 133, 107], [8, 59, 37, 1, 60, 38], [8, 44, 20, 4, 45, 21], [12, 33, 11, 4, 34, 12], [3, 145, 115, 1, 146, 116], [4, 64, 40, 5, 65, 41], [11, 36, 16, 5, 37, 17], [11, 36, 12, 5, 37, 13], [5, 109, 87, 1, 110, 88], [5, 65, 41, 5, 66, 42], [5, 54, 24, 7, 55, 25], [11, 36, 12], [5, 122, 98, 1, 123, 99], [7, 73, 45, 3, 74, 46], [15, 43, 19, 2, 44, 20], [3, 45, 15, 13, 46, 16], [1, 135, 107, 5, 136, 108], [10, 74, 46, 1, 75, 47], [1, 50, 22, 15, 51, 23], [2, 42, 14, 17, 43, 15], [5, 150, 120, 1, 151, 121], [9, 69, 43, 4, 70, 44], [17, 50, 22, 1, 51, 23], [2, 42, 14, 19, 43, 15], [3, 141, 113, 4, 142, 114], [3, 70, 44, 11, 71, 45], [17, 47, 21, 4, 48, 22], [9, 39, 13, 16, 40, 14], [3, 135, 107, 5, 136, 108], [3, 67, 41, 13, 68, 42], [15, 54, 24, 5, 55, 25], [15, 43, 15, 10, 44, 16], [4, 144, 116, 4, 145, 117], [17, 68, 42], [17, 50, 22, 6, 51, 23], [19, 46, 16, 6, 47, 17], [2, 139, 111, 7, 140, 112], [17, 74, 46], [7, 54, 24, 16, 55, 25], [34, 37, 13], [4, 151, 121, 5, 152, 122], [4, 75, 47, 14, 76, 48], [11, 54, 24, 14, 55, 25], [16, 45, 15, 14, 46, 16], [6, 147, 117, 4, 148, 118], [6, 73, 45, 14, 74, 46], [11, 54, 24, 16, 55, 25], [30, 46, 16, 2, 47, 17], [8, 132, 106, 4, 133, 107], [8, 75, 47, 13, 76, 48], [7, 54, 24, 22, 55, 25], [22, 45, 15, 13, 46, 16], [10, 142, 114, 2, 143, 115], [19, 74, 46, 4, 75, 47], [28, 50, 22, 6, 51, 23], [33, 46, 16, 4, 47, 17], [8, 152, 122, 4, 153, 123], [22, 73, 45, 3, 74, 46], [8, 53, 23, 26, 54, 24], [12, 45, 15, 28, 46, 16], [3, 147, 117, 10, 148, 118], [3, 73, 45, 23, 74, 46], [4, 54, 24, 31, 55, 25], [11, 45, 15, 31, 46, 16], [7, 146, 116, 7, 147, 117], [21, 73, 45, 7, 74, 46], [1, 53, 23, 37, 54, 24], [19, 45, 15, 26, 46, 16], [5, 145, 115, 10, 146, 116], [19, 75, 47, 10, 76, 48], [15, 54, 24, 25, 55, 25], [23, 45, 15, 25, 46, 16], [13, 145, 115, 3, 146, 116], [2, 74, 46, 29, 75, 47], [42, 54, 24, 1, 55, 25], [23, 45, 15, 28, 46, 16], [17, 145, 115], [10, 74, 46, 23, 75, 47], [10, 54, 24, 35, 55, 25], [19, 45, 15, 35, 46, 16], [17, 145, 115, 1, 146, 116], [14, 74, 46, 21, 75, 47], [29, 54, 24, 19, 55, 25], [11, 45, 15, 46, 46, 16], [13, 145, 115, 6, 146, 116], [14, 74, 46, 23, 75, 47], [44, 54, 24, 7, 55, 25], [59, 46, 16, 1, 47, 17], [12, 151, 121, 7, 152, 122], [12, 75, 47, 26, 76, 48], [39, 54, 24, 14, 55, 25], [22, 45, 15, 41, 46, 16], [6, 151, 121, 14, 152, 122], [6, 75, 47, 34, 76, 48], [46, 54, 24, 10, 55, 25], [2, 45, 15, 64, 46, 16], [17, 152, 122, 4, 153, 123], [29, 74, 46, 14, 75, 47], [49, 54, 24, 10, 55, 25], [24, 45, 15, 46, 46, 16], [4, 152, 122, 18, 153, 123], [13, 74, 46, 32, 75, 47], [48, 54, 24, 14, 55, 25], [42, 45, 15, 32, 46, 16], [20, 147, 117, 4, 148, 118], [40, 75, 47, 7, 76, 48], [43, 54, 24, 22, 55, 25], [10, 45, 15, 67, 46, 16], [19, 148, 118, 6, 149, 119], [18, 75, 47, 31, 76, 48], [34, 54, 24, 34, 55, 25], [20, 45, 15, 61, 46, 16]];
    r.getRSBlocks = function (y, E) {
        var x = r.getRsBlockTable(y, E);
        if (x == undefined) {
            throw new Error("bad rs block @ typeNumber:" + y + "/errorCorrectLevel:" + E)
        }
        var w = x.length / 3;
        var C = [];
        for (var A = 0; A < w; A++) {
            var B = x[A * 3 + 0];
            var F = x[A * 3 + 1];
            var D = x[A * 3 + 2];
            for (var z = 0; z < B; z++) {
                C.push(new r(F, D))
            }
        }
        return C
    };
    r.getRsBlockTable = function (w, i) {
        switch (i) {
            case f.L:
                return r.RS_BLOCK_TABLE[(w - 1) * 4 + 0];
            case f.M:
                return r.RS_BLOCK_TABLE[(w - 1) * 4 + 1];
            case f.Q:
                return r.RS_BLOCK_TABLE[(w - 1) * 4 + 2];
            case f.H:
                return r.RS_BLOCK_TABLE[(w - 1) * 4 + 3];
            default:
                return undefined
        }
    };

    function m() {
        this.buffer = [];
        this.length = 0
    }

    m.prototype = {
        get: function (i) {
            var w = Math.floor(i / 8);
            return ((this.buffer[w] >>> (7 - i % 8)) & 1) == 1
        }, put: function (w, y) {
            for (var x = 0; x < y; x++) {
                this.putBit(((w >>> (y - x - 1)) & 1) == 1)
            }
        }, getLengthInBits: function () {
            return this.length
        }, putBit: function (w) {
            var i = Math.floor(this.length / 8);
            if (this.buffer.length <= i) {
                this.buffer.push(0)
            }
            if (w) {
                this.buffer[i] |= (128 >>> (this.length % 8))
            }
            this.length++
        }
    };
    var n = [[17, 14, 11, 7], [32, 26, 20, 14], [53, 42, 32, 24], [78, 62, 46, 34], [106, 84, 60, 44], [134, 106, 74, 58], [154, 122, 86, 64], [192, 152, 108, 84], [230, 180, 130, 98], [271, 213, 151, 119], [321, 251, 177, 137], [367, 287, 203, 155], [425, 331, 241, 177], [458, 362, 258, 194], [520, 412, 292, 220], [586, 450, 322, 250], [644, 504, 364, 280], [718, 560, 394, 310], [792, 624, 442, 338], [858, 666, 482, 382], [929, 711, 509, 403], [1003, 779, 565, 439], [1091, 857, 611, 461], [1171, 911, 661, 511], [1273, 997, 715, 535], [1367, 1059, 751, 593], [1465, 1125, 805, 625], [1528, 1190, 868, 658], [1628, 1264, 908, 698], [1732, 1370, 982, 742], [1840, 1452, 1030, 790], [1952, 1538, 1112, 842], [2068, 1628, 1168, 898], [2188, 1722, 1228, 958], [2303, 1809, 1283, 983], [2431, 1911, 1351, 1051], [2563, 1989, 1423, 1093], [2699, 2099, 1499, 1139], [2809, 2213, 1579, 1219], [2953, 2331, 1663, 1273]];

    function t() {
        return typeof CanvasRenderingContext2D != "undefined"
    }

    function g() {
        var i = false;
        var x = navigator.userAgent;
        if (/android/i.test(x)) {
            i = true;
            var w = x.toString().match(/android ([0-9]\.[0-9])/i);
            if (w && w[1]) {
                i = parseFloat(w[1])
            }
        }
        return i
    }

    var e = (function () {
        var i = function (w, x) {
            this._el = w;
            this._htOption = x
        };
        i.prototype.draw = function (z) {
            var G = this._htOption;
            var E = this._el;
            var w = z.getModuleCount();
            var C = Math.floor(G.width / w);
            var D = Math.floor(G.height / w);
            this.clear();

            function A(H, J) {
                var K = document.createElementNS("http://www.w3.org/2000/svg", H);
                for (var I in J) {
                    if (J.hasOwnProperty(I)) {
                        K.setAttribute(I, J[I])
                    }
                }
                return K
            }

            var B = A("svg", {
                "viewBox": "0 0 " + String(w) + " " + String(w),
                "width": "100%",
                "height": "100%",
                "fill": G.colorLight
            });
            B.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");
            E.appendChild(B);
            B.appendChild(A("rect", {"fill": G.colorLight, "width": "100%", "height": "100%"}));
            B.appendChild(A("rect", {"fill": G.colorDark, "width": "1", "height": "1", "id": "template"}));
            for (var F = 0; F < w; F++) {
                for (var y = 0; y < w; y++) {
                    if (z.isDark(F, y)) {
                        var x = A("use", {"x": String(y), "y": String(F)});
                        x.setAttributeNS("http://www.w3.org/1999/xlink", "href", "#template");
                        B.appendChild(x)
                    }
                }
            }
        };
        i.prototype.clear = function () {
            while (this._el.hasChildNodes()) {
                this._el.removeChild(this._el.lastChild)
            }
        };
        return i
    })();
    var b = document.documentElement.tagName.toLowerCase() === "svg";
    var h = b ? e : !t() ? (function () {
        var i = function (w, x) {
            this._el = w;
            this._htOption = x
        };
        i.prototype.draw = function (A) {
            var H = this._htOption;
            var E = this._el;
            var w = A.getModuleCount();
            var B = Math.floor(H.width / w);
            var D = Math.floor(H.height / w);
            var F = ['<table style="border:0;border-collapse:collapse;">'];
            for (var G = 0; G < w; G++) {
                F.push("<tr>");
                for (var y = 0; y < w; y++) {
                    F.push('<td style="border:0;border-collapse:collapse;padding:0;margin:0;width:' + B + "px;height:" + D + "px;background-color:" + (A.isDark(G, y) ? H.colorDark : H.colorLight) + ';"></td>')
                }
                F.push("</tr>")
            }
            F.push("</table>");
            E.innerHTML = F.join("");
            var C = E.childNodes[0];
            var z = (H.width - C.offsetWidth) / 2;
            var x = (H.height - C.offsetHeight) / 2;
            if (z > 0 && x > 0) {
                C.style.margin = x + "px " + z + "px"
            }
        };
        i.prototype.clear = function () {
            this._el.innerHTML = ""
        };
        return i
    })() : (function () {
        function i() {
            this._elImage.src = this._elCanvas.toDataURL("image/png");
            this._elImage.style.display = "block";
            this._elCanvas.style.display = "none"
        }

        if (this._android && this._android <= 2.1) {
            var x = 1 / window.devicePixelRatio;
            var y = CanvasRenderingContext2D.prototype.drawImage;
            CanvasRenderingContext2D.prototype.drawImage = function (B, G, F, H, D, J, I, A, E) {
                if (("nodeName" in B) && /img/i.test(B.nodeName)) {
                    for (var C = arguments.length - 1; C >= 1; C--) {
                        arguments[C] = arguments[C] * x
                    }
                } else {
                    if (typeof A == "undefined") {
                        arguments[1] *= x;
                        arguments[2] *= x;
                        arguments[3] *= x;
                        arguments[4] *= x
                    }
                }
                y.apply(this, arguments)
            }
        }

        function z(A, E) {
            var B = this;
            B._fFail = E;
            B._fSuccess = A;
            if (B._bSupportDataURI === null) {
                var D = document.createElement("img");
                var F = function () {
                    B._bSupportDataURI = false;
                    if (B._fFail) {
                        B._fFail.call(B)
                    }
                };
                var C = function () {
                    B._bSupportDataURI = true;
                    if (B._fSuccess) {
                        B._fSuccess.call(B)
                    }
                };
                D.onabort = F;
                D.onerror = F;
                D.onload = C;
                D.src = "data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
                return
            } else {
                if (B._bSupportDataURI === true && B._fSuccess) {
                    B._fSuccess.call(B)
                } else {
                    if (B._bSupportDataURI === false && B._fFail) {
                        B._fFail.call(B)
                    }
                }
            }
        }

        var w = function (A, B) {
            this._bIsPainted = false;
            this._android = g();
            this._htOption = B;
            this._elCanvas = document.createElement("canvas");
            this._elCanvas.width = B.width;
            this._elCanvas.height = B.height;
            A.appendChild(this._elCanvas);
            this._el = A;
            this._oContext = this._elCanvas.getContext("2d");
            this._bIsPainted = false;
            this._elImage = document.createElement("img");
            this._elImage.alt = "Scan me!";
            this._elImage.style.display = "none";
            this._el.appendChild(this._elImage);
            this._bSupportDataURI = null
        };
        w.prototype.draw = function (F) {
            var N = this._elImage;
            var K = this._oContext;
            var M = this._htOption;
            var C = F.getModuleCount();
            var H = M.width / C;
            var I = M.height / C;
            var D = Math.round(H);
            var J = Math.round(I);
            N.style.display = "none";
            this.clear();
            for (var L = 0; L < C; L++) {
                for (var E = 0; E < C; E++) {
                    var G = F.isDark(L, E);
                    var A = E * H;
                    var B = L * I;
                    K.strokeStyle = G ? M.colorDark : M.colorLight;
                    K.lineWidth = 1;
                    K.fillStyle = G ? M.colorDark : M.colorLight;
                    K.fillRect(A, B, H, I);
                    K.strokeRect(Math.floor(A) + 0.5, Math.floor(B) + 0.5, D, J);
                    K.strokeRect(Math.ceil(A) - 0.5, Math.ceil(B) - 0.5, D, J)
                }
            }
            this._bIsPainted = true
        };
        w.prototype.makeImage = function () {
            if (this._bIsPainted) {
                z.call(this, i)
            }
        };
        w.prototype.isPainted = function () {
            return this._bIsPainted
        };
        w.prototype.clear = function () {
            this._oContext.clearRect(0, 0, this._elCanvas.width, this._elCanvas.height);
            this._bIsPainted = false
        };
        w.prototype.round = function (A) {
            if (!A) {
                return A
            }
            return Math.floor(A * 1000) / 1000
        };
        return w
    })();

    function a(y, A) {
        var x = 1;
        var B = j(y);
        for (var z = 0, w = n.length; z <= w; z++) {
            var C = 0;
            switch (A) {
                case f.L:
                    C = n[z][0];
                    break;
                case f.M:
                    C = n[z][1];
                    break;
                case f.Q:
                    C = n[z][2];
                    break;
                case f.H:
                    C = n[z][3];
                    break
            }
            if (B <= C) {
                break
            } else {
                x++
            }
        }
        if (x > n.length) {
            throw new Error("Too long data")
        }
        return x
    }

    function j(w) {
        var i = encodeURI(w).toString().replace(/\%[0-9a-fA-F]{2}/g, "a");
        return i.length + (i.length != w ? 3 : 0)
    }

    l = function (x, y) {
        this._htOption = {
            width: 256,
            height: 256,
            typeNumber: 4,
            colorDark: "#000000",
            colorLight: "#ffffff",
            correctLevel: f.H
        };
        if (typeof y === "string") {
            y = {text: y}
        }
        if (y) {
            for (var w in y) {
                this._htOption[w] = y[w]
            }
        }
        if (typeof x == "string") {
            x = document.getElementById(x)
        }
        if (this._htOption.useSVG) {
            h = e
        }
        this._android = g();
        this._el = x;
        this._oQRCode = null;
        this._oDrawing = new h(this._el, this._htOption);
        if (this._htOption.text) {
            this.makeCode(this._htOption.text)
        }
    };
    l.prototype.makeCode = function (i) {
        this._oQRCode = new s(a(i, this._htOption.correctLevel), this._htOption.correctLevel);
        this._oQRCode.addData(i);
        this._oQRCode.make();
        this._el.title = i;
        this._oDrawing.draw(this._oQRCode);
        this.makeImage()
    };
    l.prototype.makeImage = function () {
        if (typeof this._oDrawing.makeImage == "function" && (!this._android || this._android >= 3)) {
            this._oDrawing.makeImage()
        }
    };
    l.prototype.clear = function () {
        this._oDrawing.clear()
    };
    l.CorrectLevel = f;
    v("QRCode", l)
});